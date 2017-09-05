/**   
* @Title: ClaimLaborMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(索赔工时维护Action) 
* @author Administrator   
* @date 2010-6-1 上午11:05:38 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.serviceActivity.BaseImport;
import com.infodms.dms.actions.common.ClaimCommonAction;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.ClaimLaborDao;
import com.infodms.dms.dao.claim.basicData.PartsLegalDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrAdditionalitemPO;
import com.infodms.dms.po.TtAsWrFaultLegalPO;
import com.infodms.dms.po.TtAsWrFaultPartsPO;
import com.infodms.dms.po.TtAsWrFaultPartsTempPO;
import com.infodms.dms.po.TtAsWrPartLegalExtPO;
import com.infodms.dms.po.TtAsWrPartLegallDetailPO;
import com.infodms.dms.po.TtAsWrPartsAssemblyPO;
import com.infodms.dms.po.TtAsWrPartLegalPO;
import com.infodms.dms.po.TtAsWrTrobleMapPO;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.PageQuery;

import flex.messaging.io.ArrayList;

/** 
 * @ClassName: ClaimLaborMain 
 * @Description: TODO(索赔工时维护Action) 
 * @author Administrator 
 * @date 2013-04-12 上午11:05:38 
 *  
 */
public class PartsLegal extends BaseImport {
	private Logger logger = Logger.getLogger(PartsAssembly.class);
	private final PartsLegalDao dao = PartsLegalDao.getInstance();
	private final String VIEW_URL = "/jsp/claim/basicData/partsLegalIndex.jsp";//主页面（查询）
	private final String MODIFY_URL = "/jsp/claim/basicData/partsLegalModify.jsp";//修改页面（查询）
	private final String ADD_URL = "/jsp/claim/basicData/partsLrgalAdd.jsp";//新增页面（查询）
	private final String part_import_Url = "/jsp/claim/basicData/partInfoImportLegal.jsp";
	private final String part_import_Failure_Url = "/jsp/claim/basicData/partImportFailure.jsp";
	private final String ADD_PART_URL = "/jsp/claim/basicData/addPartLegal.jsp";//新增配件页面
	
	private final ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
	/**
	 * 
	* @Title: claimLaborInit 
	* @Description: TODO(总成维护跳转界面) 
	* @param    
	* @return void  
	* @throws
	 */
	public void PartsLegalMain(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String,Object>> list = dao.viewAssemblyDetail();
			act.setOutData("list", list);
			act.setForword(VIEW_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	* @Description: 新增配件
	* @param 
	* @date 2013-11-25上午11:05:12
	* @throws luole
	*
	 */
	public void addParts(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String faultId = request.getParamValue("faultId");
			TtAsWrPartLegalPO temppo = new TtAsWrPartLegalPO();
			temppo.setPartLegalId(Long.parseLong(faultId));
			List<TtAsWrPartLegalPO> list = dao.select(temppo);
			TtAsWrPartLegalPO po = list.get(0);
			act.setOutData("TtAsWrPartLegalPO", po);
			act.setForword(ADD_PART_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	/**
	* @Description: 保存新增配件
	* @param 
	* @date 2013-11-25下午1:40:45
	* @throws luole
	*
	 */
	public void saveAddParts(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String faultId = request.getParamValue("ID");
			String partCode = request.getParamValue("PART_CODE");
			String partName = request.getParamValue("PART_NAME");
			TtAsWrPartLegallDetailPO po = new TtAsWrPartLegallDetailPO();
			po.setPartCode(partCode);
			po.setPartLegalId(Utility.getLong(faultId));
			List<TtAsWrPartLegallDetailPO> list = dao.select(po);
			if(list==null || list.size()<=0){
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(new Date());
				po.setId(Utility.getLong(SequenceManager.getSequence("")));
				po.setPartLegalId(Utility.getLong(faultId));
				po.setPartCode(partCode);
				po.setPartName(partName);
				po.setStatus(Constant.IF_TYPE_YES);
				po.setIsDe(0);
				dao.insert(po);
				act.setOutData("fId", faultId);
				act.setOutData("flag", true);
			}else{
				act.setOutData("flag", false);
				act.setOutData("msg", "新增配件已在该零件法定名称下");
			}
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
	public void PartsLegalView(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String parts_accembly_code= request.getParamValue("parts_legal_code");
			String parts_accembly_name= request.getParamValue("parts_legal_name");
			String checkUserSel = request.getParamValue("checkUserSel");
			String status= request.getParamValue("TYPE_CODE");
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<TtAsWrPartLegalExtPO>  ps =dao.getPartsLrgalView(parts_accembly_code, parts_accembly_name, status, checkUserSel, Constant.PAGE_SIZE, curPage);
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
	public void delPartsLegal(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String recesel [] = request.getParamValues("recesel");
			TtAsWrPartLegalPO po = new TtAsWrPartLegalPO();
			TtAsWrPartLegalPO po1 = new TtAsWrPartLegalPO();
			for(String id:recesel){
				po.setPartLegalId(Long.valueOf(id));
				po1.setStatus(Constant.STATUS_DISABLE);
				dao.update(po, po1);
			}
			
			act.setOutData("falg", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"零件法定名称维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	
	
	/**********
	 * add user:xiongchuan
	 * add date:2013-04-24
	 * remark： 零件法定名称对应配件明细删除功能
	 */
	public void delPartsLegalDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String recesel [] = request.getParamValues("recesel");
			TtAsWrPartLegallDetailPO po = new TtAsWrPartLegallDetailPO();
			TtAsWrPartLegallDetailPO po1 = new TtAsWrPartLegallDetailPO();
			for(String id:recesel){
				po.setId(Long.valueOf(id));
				dao.delete(po);
			}
			
			act.setOutData("falg", "success");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"零件法定名称维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**********
	 * add user:xiongchuan
	 * add date:2013-04-12
	 * remark： 总成维护新增跳转功能
	 */
	public void addPartsLrgalFoward(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String,Object>> list = dao.viewAssemblyDetail();
			Long id = Utility.getLong(SequenceManager.getSequence(""));
			act.setOutData("list", list);
			act.setOutData("id", id);
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
	public void modifyPartsLegal(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String recesel = request.getParamValue("fID");
			System.out.println(recesel);
			//TtAsWrPartLegalPO po = new TtAsWrPartLegalPO();
			//po.setPartLegalId(Long.valueOf(recesel));
			List<Map<String,Object>> lit = null;
			
				lit = dao.modifyPartsLegal(recesel);
			
			
			List<Map<String,Object>> list = dao.viewAssemblyDetail();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			act.setOutData("list", list);
			act.setOutData("lit",  lit.get(0));
			 List<Map<String,Object>> detailList = dao.viewPartLegallDetail(recesel, Constant.PAGE_SIZE, curPage);
				act.setOutData("detailList", detailList);
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
	public void savePartsLrgal(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String id = request.getParamValue("ID");
			String partsLrgalCode = request.getParamValue("parts_legal_code");
			String partsLrgalName = request.getParamValue("parts_legal_name");
			String TYPE_CODE = request.getParamValue("TYPE_CODE");
			String checkUserSel = request.getParamValue("checkUserSel");
			TtAsWrPartLegalPO po = new TtAsWrPartLegalPO();
			TtAsWrPartLegalPO po1 = new TtAsWrPartLegalPO();
		    po.setPartLegalId(Long.valueOf(id));
		    po1.setPartLegalCode(partsLrgalCode);
		    po1.setPartLegalName(partsLrgalName);
		    po1.setStatus(Integer.valueOf(TYPE_CODE));
		    po1.setPartsAssemblyId(Long.valueOf(checkUserSel));
			dao.update(po, po1);
			act.setOutData("flag", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"总成修改报错");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	
	public void addPartsLrgal(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String id = request.getParamValue("ID");
			String partsLrgalCode = request.getParamValue("parts_legal_code");
			String partsLrgalName = request.getParamValue("parts_legal_name");
			String TYPE_CODE = request.getParamValue("TYPE_CODE");
			String checkUserSel = request.getParamValue("checkUserSel");
			TtAsWrPartLegalPO po1 = new TtAsWrPartLegalPO();
		    po1.setPartLegalId(Long.valueOf(id));
		    po1.setPartLegalCode(partsLrgalCode);
		    po1.setPartLegalName(partsLrgalName);
		    po1.setPartsAssemblyId(Long.valueOf(checkUserSel));
		    po1.setStatus(Integer.valueOf(TYPE_CODE));
			dao.insert(po1);
			act.setOutData("flag", "true");
			act.setOutData("id", id);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"总成修改报错");
			logger.error(logonUser,e1);
			act.setOutData("flag", "false");
			act.setException(e1);
		}		
		
	}
	
	
	
	
	
	/**********
	 * add user:xiongchuan
	 * add date:2013-04-13
	 * remark： 总成维护修改保存功能
	 */
	public void addSavePartsLegal(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Long id = Utility.getLong(SequenceManager.getSequence(""));
			String partsLegalCode = request.getParamValue("parts_accembly_code");
			String partsLegalName = request.getParamValue("parts_accembly_name");
			String TYPE_CODE = request.getParamValue("TYPE_CODE");
			TtAsWrPartLegalPO po1 = new TtAsWrPartLegalPO();
		//	po1.setPartsLegalId(Long.valueOf(id));
			//po1.setPartsLegalCode(partsLegalCode);
			//po1.setPartsLegalName(partsLegalName);
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
	
		//导入零件法定名称配件信息（跳转页）
	public void partImport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
	    	String faultId=request.getParamValue("faultId");//故障法定名称Id
	    	request.setAttribute("faultId", faultId);
			act.setForword(part_import_Url);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	private void insertTtAsWrLegalParts(List<Map> list,String faultId) throws Exception{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
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
				int partCode =cells[0].getContents().length();
				if(partCode >50){
					sb.append("配件信息代码超过20位!");
					fl=false;
					break;
				}
				int partName =cells[1].getContents().length();
				if(partName >200){
					sb.append("配件信息名称超过200位!");
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
					parseCells(key, cells,faultId);
				}
			}
			request.setAttribute("faultId", faultId);
			TtAsWrFaultLegalPO result = new TtAsWrFaultLegalPO();
			result.setFaultId(Long.parseLong(faultId));
			TtAsWrFaultLegalPO resultValue = (TtAsWrFaultLegalPO)dao.select(result).get(0);
			request.setAttribute("faultList", resultValue);
			act.setForword(faultId);
		}else{
			ExcelErrors ees=new ExcelErrors();
			request.setAttribute("ees", ees);
			request.setAttribute("sb", sb.toString());
			act.setForword(part_import_Failure_Url);
		}
	}
	
	private void parseCells(String rowNum,Cell[] cells,String faultId) throws Exception{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrFaultPartsTempPO RuleListPO=new TtAsWrFaultPartsTempPO();
		StringBuffer sb=new StringBuffer();
		if(cells[0].getContents().equals("")||cells[0].getContents()==null){
			sb.append("配件代码为空!"+",");
		}else{
			RuleListPO.setPartCode(subCell(cells[0].getContents().trim()));//配件代码
		}
		if(cells[1].getContents().equals("")||cells[1].getContents()==null){
			sb.append("配件名称为空!"+",");
		}else{
			RuleListPO.setPartName(subCell(cells[1].getContents().trim()));//配件名称
		}
		RuleListPO.setId(Long.parseLong(SequenceManager.getSequence("")));//ID
		RuleListPO.setFaultId(Long.valueOf(faultId));
		RuleListPO.setStatus(Constant.IF_TYPE_YES);
		RuleListPO.setIsDe(0);
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
		dao.claimRulePartsImportAdd(RuleListPO);//把数据插入临时表
		dao.claimRulePartsImportAddMerge(RuleListPO);//把临时表的数据导入正式表，（3种选择可以成功导入，配件code相同但名称不同或者名称相同code不同，或者code和名称都不同）
	}
	
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
	
	
	public void partsImportOption(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String faultId=request.getParamValue("faultId");
			TtAsWrFaultPartsPO PartListPO = new TtAsWrFaultPartsPO();
			if (null!=faultId&&!"".equals(faultId)) {
				PartListPO.setFaultId(Long.parseLong(faultId));
			}
			//清空临时表信息
			TtAsWrFaultPartsTempPO RuleListTmpPO = new TtAsWrFaultPartsTempPO();
			dao.claimRulePartsImportDelete(RuleListTmpPO);

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
			    request.setAttribute("faultId", faultId);
			    if(1==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
			    else if(2==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
			    else if(3==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
			    else if(4==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
			    else if(5==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
				else{
					act.setForword(MODIFY_URL);
				}
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTtAsWrRuleList(list,faultId);
			}
		}catch(Exception e){   
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件清单导入");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 	
	}
	private void insertTtAsWrRuleList(List<Map> list1,String faultId) throws Exception{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		boolean fl=true;
		StringBuffer sb=new StringBuffer();
		if(null==list1){
			list1=new ArrayList();
		}
		for(int i=0;i<list1.size();i++){
			Map map=list1.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
				int partCode =cells[0].getContents().length();
				if(partCode >50){
					sb.append("配件信息代码超过20位!");
					fl=false;
					break;
				}
				int partName =cells[1].getContents().length();
				if(partName >200){
					sb.append("配件信息名称超过200位!");
					fl=false;
					break;
				}
			}
		}
		if(fl==true){
			for(int i=0;i<list1.size();i++){
				Map map=list1.get(i);
				if(null==map){
					map=new HashMap<String, Cell[]>();
				}
				Set<String> keys=map.keySet();
				Iterator it=keys.iterator();
				String key="";
				while(it.hasNext()){
					key=(String)it.next();
					Cell[] cells=(Cell[])map.get(key);
					parseCells(key, cells,faultId);
				}
			}
			request.setAttribute("faultId", faultId);
			TtAsWrPartLegalPO po = new TtAsWrPartLegalPO();
			po.setPartLegalId(Long.valueOf(faultId));
			List<Map<String,Object>> lit = dao.modifyPartsLegal(faultId);
			List<Map<String,Object>> list = dao.viewAssemblyDetail();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			act.setOutData("list", list);
			act.setOutData("lit", lit.get(0));
			 List<Map<String,Object>> detailList = dao.viewPartLegallDetail(faultId, Constant.PAGE_SIZE, curPage);
				act.setOutData("detailList", detailList);
			act.setForword(MODIFY_URL);
		}else{
			ExcelErrors ees=new ExcelErrors();
			request.setAttribute("ees", ees);
			request.setAttribute("sb", sb.toString());
			act.setForword(part_import_Failure_Url);
		}
	}
}
