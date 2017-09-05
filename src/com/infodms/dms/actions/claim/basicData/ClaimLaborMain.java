/**   
* @Title: ClaimLaborMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(索赔工时维护Action) 
* @author Administrator   
* @date 2010-6-1 上午11:05:38 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.ClaimCommonAction;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.basicData.ClaimLaborDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrAdditionalitemPO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrTrobleMapPO;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimLaborMain 
 * @Description: TODO(索赔工时维护Action) 
 * @author Administrator 
 * @date 2010-6-1 上午11:05:38 
 *  
 */
public class ClaimLaborMain extends BaseAction{
	private Logger logger = Logger.getLogger(ClaimLaborMain.class);
	private final ClaimLaborDao dao = ClaimLaborDao.getInstance();
	private final String CLAIM_LABOR_URL = "/jsp/claim/basicData/claimLaborIndex.jsp";//主页面（查询）
	private final String CLAIM_LABOR_ADD_URL = "/jsp/claim/basicData/claimLaborAdd.jsp";//新增页面
	private final String CLAIM_LABOR_UPDATE_URL = "/jsp/claim/basicData/claimLaborModify.jsp";//修改页面
	private final String CLAIM_LABOR_UPDATE_URL2 = "/jsp/claim/basicData/claimLaborMultiModify.jsp";//批量修改页面
	private final String ADDITIONAL_ADD_URL = "/jsp/claim/basicData/addLaborAdd.jsp";//附加工时新增页面
	private final String ADDITIONAL_UPDATE_URL = "/jsp/claim/basicData/addLaborModify.jsp";//附加工时修改页面
	private final String BUSINESS_ADD_URL = "/jsp/claim/basicData/troubleAdd.jsp";//故障代码新增页面
	private final String CLAIM_LABOR_DETAIL_URL = "/jsp/claim/basicData/claimLaborCodeDetail.jsp";//详细信息页面
	private final String CLAIM_LABOR_BATCH_MODIFY = "/jsp/claim/basicData/batchClaimLaborModify.jsp";//修改页面
	private final String CLAIM_LABOR_QUERY_URL = "/jsp/claim/basicData/claimLaborQueryIndex.jsp";
	private final String PART_THREE_BAGS_URL = "/jsp/claim/basicData/partThreeBagsQueryindex.jsp";
	private final String PART_THREE_BAGS_DETAIL_URL = "/jsp/claim/basicData/partThreeBagsDetail.jsp";
	private final String INPORT_PER = "/jsp/claim/basicData/importPer3.jsp";
	private  final String INPUT_ERROR_URL= "/jsp/claim/basicData/inputerror.jsp";//数据导入出错页面
	private  final String INPORT_SURE = "/jsp/claim/basicData/importSureDo3.jsp";//数据导入确认页面
	private final ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
	/**
	 * 
	* @Title: claimLaborInit 
	* @Description: TODO(索赔工时维护初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborInit(){
		
		
		String flag = "0";
		try {
			
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(loginUser);
			//modify at 2010-07-23 增加是否是新增或修改返回的标识，flag=1：是，flag= 0：否
			flag = request.getParamValue("reFlag");//
			act.setOutData("REFLAG", flag);
			//modify at 2010-07-23 end
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("1",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setForword(CLAIM_LABOR_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时维护");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	
	//zhumingwei 2011-8-10
	public void claimLaborQueryInit(){
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(loginUser);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("1",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setForword(CLAIM_LABOR_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时维护");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 批量导入跳转
	 */
	public void inportPer(){
		try{
			act.setForword(INPORT_PER);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(loginUser, be);
			act.setException(be);
		}
	}
	
	/**
	 * 批量导入模板下载
	 */
	@SuppressWarnings("unchecked")
	public void download(){
		try {
			List<List<Object>> list = new ArrayList<List<Object>>();
			String[] listHead = new String[7];
			listHead[0]="车型组";
			listHead[1]="工时代码";
			listHead[2]="工时名称";
			listHead[3]="工时大类代码";
			listHead[4]="工时大类名称";
			listHead[5]="工时系数";
			listHead[6]="索赔工时";
			
			List list1=new ArrayList();
			String[]detail=new String[7];
			detail[0]="B33DK00";
			detail[1]="11010010101";
			detail[2]="发动机裸机-更换";
			detail[3]="GS03";
			detail[4]="更换";
			detail[5]="1.00";
			detail[6]="9.00";
			list1.add(detail);
	    	com.infodms.dms.actions.claim.basicData.ToExcel.toExceUtil(response, request, listHead, list1,"索赔工时维护.xls");
	   /**
	    *  以下 是 只有表头，木有示例 数据的生成方法
	    */

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔工时单价设定");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void excelOperate(){
		
		
		
		try {
			List<Map<String,String>> errorInfo= new ArrayList<Map<String,String>>();
			
			long maxSize=1024*1024*5;
			BaseImport b=new BaseImport();
			int errNum=b.insertIntoTmp(request, "uploadFile",7,7,maxSize);
			String err="";
			
			if(errNum!=0){
				switch (errNum) {
				case 1:
					err+="文件列数过多!";
					break;
				case 2:
					err+="空行不能大于三行!";
					break;
				case 3:
					err+="文件不能为空!";
					break;
				case 4:
					err+="文件不能为空!";
					break;
				case 5:
					err+="文件不能大于!";
					break;
				default:
					break;
				}
			}
			if(!"".equals(err)){
				act.setOutData("error", err);
				act.setForword(INPUT_ERROR_URL);
			}else{
				List<Map> list=b.getMapList();
				List<Map<String,String>> voList = new ArrayList<Map<String,String>>();
				loadVoList(voList,list, errorInfo);
				if(errorInfo.size()>0){
					act.setOutData("errorInfo", errorInfo);
					act.setForword(INPUT_ERROR_URL);
				}else{
					act.setOutData("list", voList);
					act.setForword(INPORT_SURE);
				}
				
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = null;
			if(e instanceof BizException){
				e1 = (BizException)e;
			}else{
				new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			}
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadVoList(List<Map<String,String>> voList,List<Map> list,List<Map<String,String>> errorInfo) throws Exception{
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
					Map<String, String> tempmap = new HashMap<String, String>();
					if ("".equals(cells[0].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "车型组");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						String sql = "select * from TT_AS_WR_MODEL_GROUP where wrgroup_code="+"'"+cells[0].getContents().trim()+"'";
						List dealerList = dao.select(TtAsWrModelGroupPO.class, sql, null);
						if (dealerList.isEmpty()) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key+ "行");
							errormap.put("2", "车型组");
							errormap.put("3", "不存在");
							errorInfo.add(errormap);
						} 
					}
					tempmap.put("1", cells[0].getContents().trim());
					if ("".equals(cells[1].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "工时代码");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						String sql = "select * from TT_AS_WR_WRLABINFO t1 left join TT_AS_WR_MODEL_GROUP t2 on t1.wrgroup_id=t2.wrgroup_id where t1.labour_code ="+"'"+cells[1].getContents().trim()+"'" +"and t2.wrgroup_code="+"'"+cells[0].getContents().trim()+"'";
						List dealerList = dao.select(TtAsWrWrlabinfoPO.class, sql, null);
						if (!dealerList.isEmpty()) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key+ "行");
							errormap.put("2", "该车型组配对的工时代码");
							errormap.put("3", "已存在");
							errorInfo.add(errormap);
						} 
					}
					
					tempmap.put("2", cells[1].getContents().trim());
					
					if ("".equals(cells[2].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "工时名称");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} 
					
					tempmap.put("3", cells[2].getContents().trim());
					
					if ("".equals(cells[3].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "工时大类代码");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					}  else {
						String sql2 = "select * from TT_AS_WR_WRLABINFO where labour_code ="+"'"+cells[3].getContents().trim()+"'";
						List<TtAsWrWrlabinfoPO> paterIdList = dao.select(TtAsWrWrlabinfoPO.class, sql2, null);
						if (paterIdList.isEmpty()) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key+ "行");
							errormap.put("2", "工时大类代码");
							errormap.put("3", "不存在");
							errorInfo.add(errormap);
						} 
					}
					
					tempmap.put("4", cells[3].getContents().trim());
					
					if ("".equals(cells[4].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "工时大类名称");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} 
					
					tempmap.put("5", cells[4].getContents().trim());
					
					
					if ("".equals(cells[5].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "工时系数");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						if (cells[5].getContents().trim().split("\\.").length <=1 || cells[5].getContents().trim().split("\\.")[1].length() != 2) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "工时系数");
							errormap.put("3", "必须是2位小数");
							errorInfo.add(errormap);
						}
					}
					tempmap.put("6", cells[5].getContents().trim());
					
					if ("".equals(cells[6].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "索赔工时");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						if (cells[6].getContents().trim().split("\\.").length <= 1 || cells[6].getContents().trim().split("\\.")[1].length() != 2) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "索赔工时");
							errormap.put("3", "必须是2位小数");
							errorInfo.add(errormap);
						}
					}
					tempmap.put("7", cells[6].getContents().trim());
					voList.add(tempmap);
			}	
		}
	}
	
	@SuppressWarnings("unchecked")
	public void importAdd(){
		
		Long oemCompanyId=GetOemcompanyId.getOemCompanyId(loginUser);
		act.getResponse().setContentType("application/json");
		try {
			String strCount = CommonUtils.checkNull(request.getParamValue("count")); //导入数据总条数
			
			int count = Integer.parseInt(strCount);
			for(int i=1;i<count; i++){
				String cxz = CommonUtils.checkNull(request.getParamValue("cxz"+i));//车型大类
				String gsdm = CommonUtils.checkNull(request.getParamValue("gsdm"+i));//车型大类
				String gsmc = CommonUtils.checkNull(request.getParamValue("gsmc"+i));//工时单价
				String gsdlCode = CommonUtils.checkNull(request.getParamValue("gsdlCode"+i));//经销商ID
				String gsdlName = CommonUtils.checkNull(request.getParamValue("gsdlName"+i));//经销商ID
				String gsxs = CommonUtils.checkNull(request.getParamValue("gsxs"+i));//经销商ID
				String spgs = CommonUtils.checkNull(request.getParamValue("spgs"+i));//经销商ID
				
				String sql = "select * from TT_AS_WR_MODEL_GROUP where wrgroup_code ="+"'"+cxz+"'";
				List<TtAsWrModelGroupPO> cxzList = dao.select(TtAsWrModelGroupPO.class, sql, null);
				String sql2 = "select * from TT_AS_WR_WRLABINFO where labour_code ="+"'"+gsdlCode+"'";
				List<TtAsWrWrlabinfoPO> paterIdList = dao.select(TtAsWrWrlabinfoPO.class, sql2, null);
				TtAsWrWrlabinfoPO po = new TtAsWrWrlabinfoPO() ;
				// 此经销商对应的工时大类不存在则执行添加操作
					Long logId = Long.parseLong(SequenceManager.getSequence(""));
					po.setId(logId);
					po.setWrgroupId(cxzList.get(0).getWrgroupId());
					po.setCnDes(gsmc);
					po.setLabourCode(gsdm);
					po.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_03);
					po.setLabourHour(Utility.getFloat(spgs));
					po.setLabourQuotiety(Utility.getFloat(gsxs));
					po.setPaterId((paterIdList.get(0)).getId());
					po.setOemCompanyId(oemCompanyId);
					po.setCreateBy(loginUser.getUserId());
					po.setCreateDate(new Date());
					dao.insert(po);
					
//					addpo.setCnDes(laborName);
//					addpo.setLabourCode(laborCode);
//					addpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_03);
//					addpo.setLabourHour(labourHour);
//					addpo.setLabourQuotiety(labourQuotiety);
//					addpo.setPaterId(Utility.getLong(paterId));//大类
//					addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
//					addpo.setOemCompanyId(oemCompanyId);
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时维护维护导入到数据库中");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
}
	
	/**
	 * 
	* @Title: getWrModelGroupList 
	* @Description: TODO(获取索赔工时车型组列表) 
	* @param @return   
	* @return String  
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	public String getWrModelGroupList(String type){
		List list = dao.getClaimWrModelGroup(Constant.WR_MODEL_GROUP_TYPE_01);
		String retStr="";
		if("1".equals(type)){
			retStr+="<option value=\'\'>-请选择-</option>";
		}
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				HashMap map = new HashMap();
				map=(HashMap)list.get(i);
				retStr+="<option value=\'"+map.get("WRGROUP_ID")+"\'>"+map.get("WRGROUP_CODE")+"</option>";
			}
		}
		return retStr;
	}
	/**
	 * 
	* @Title: claimLaborQuery 
	* @Description: TODO(索赔工时查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborQuery() {
		
		
		Long oemCompanyId=GetOemcompanyId.getOemCompanyId(loginUser);    
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//代码列别
				String wrgroupId = request.getParamValue("WRGROUP_ID");//车型组
				String labourCode = request.getParamValue("LABOUR_CODE");//工时代码
				String cnDes = request.getParamValue("CN_DES");//中文说明(工时名称)
				//modify at 2010-07-20 start
				String labourCodebig = request.getParamValue("LABOUR_CODE_BIG");//工时大类代码
				String cnDesbig = request.getParamValue("CN_DES_BIG");//工时大类名称	
				//modify at 2010-07-20 end
				//拼sql的查询条件
				if (Utility.testString(wrgroupId)) {
					sb.append(" and taww.wrgroup_id in("+wrgroupId+") ");
				}
				//modify 工时代码不区分大小写
				if (Utility.testString(labourCode)) {
					sb.append(" and upper(taww.labour_code) like ? ");
					params.add("%"+labourCode.toUpperCase()+"%");
				}
				if (Utility.testString(cnDes)) {
					sb.append(" and taww.cn_des like ? ");
					params.add("%"+cnDes+"%");
				}
				//modify at 2010-07-20 start
				//增加工时大类代码、名称查询条件
				if(Utility.testString(labourCodebig) || Utility.testString(cnDesbig)){
					sb.append(" and taww.pater_id in (select t2.id\n" );
					sb.append(" from tt_as_wr_wrlabinfo t2\n" );
					sb.append(" where 1=1\n" );
					if(Utility.testString(labourCodebig)){
						sb.append(" and upper(t2.labour_code) like ? \n" );
						params.add("%"+labourCodebig.toUpperCase()+"%");
					}
					if(Utility.testString(cnDesbig)){
						sb.append(" and t2.cn_des like ? \n" );
						params.add("%"+cnDesbig+"%");
					}
					sb.append(" and t2.tree_code in (1, 2))\n");
				}
//				modify at 2010-07-20 end
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.claimLaborQuery(Constant.PAGE_SIZE, curPage,oemCompanyId,sb.toString(),params,request);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(loginUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	//设置失效
	public void  Failure(){
	String type = 	getParam("type");
	   if ("updates".equals(type)) {
		   int res =	dao.updateclaimLabors(request,loginUser);
			act.setOutData("succ", res);
	   }else {
		int res =	dao.updateclaimLabor(request,loginUser);
		act.setOutData("succ", res);
	   }
	}
	
	//zhumingwei 2011-8-10
	public void claimLaborQuery11() {
		
		
		Long oemCompanyId=GetOemcompanyId.getOemCompanyId(loginUser);
		StringBuffer sb = new StringBuffer();
		try {
			
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//代码列别
				String wrgroupId = request.getParamValue("WRGROUP_ID");//车型组
				String labourCode = request.getParamValue("LABOUR_CODE");//工时代码
				String cnDes = request.getParamValue("CN_DES");//中文说明(工时名称)
				String level = request.getParamValue("level");
				//拼sql的查询条件
				if (Utility.testString(wrgroupId)) {
					sb.append(" and taww.wrgroup_id = '"+wrgroupId+"' ");
				}
				//modify 工时代码不区分大小写
				if (Utility.testString(labourCode)) {
					sb.append(" and upper(taww.labour_code) like '%"+labourCode.toUpperCase()+"%' ");
				}
				if (Utility.testString(cnDes)) {
					sb.append(" and taww.cn_des like '%"+cnDes+"%' ");
				}
				if (Utility.testString(level)) {
					if("一般授权".equals(level)){
						sb.append(" and wa.approval_level = 200 ");
					}
					if("大区授权".equals(level)){
						sb.append(" and wa.approval_level = 400 ");
					}
					if("主管授权".equals(level)){
						sb.append(" and wa.approval_level = 600 ");
					}
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.claimLaborQuery11(Constant.PAGE_SIZE, curPage,oemCompanyId,sb.toString());
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(loginUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimLaborAddInit 
	* @Description: TODO(索赔工时新增初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborAddInit(){
		
		
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(loginUser);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setForword(CLAIM_LABOR_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	
	public void batchUpdateMain(){
		
		
		try {
		
			act.setForword(CLAIM_LABOR_BATCH_MODIFY);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborAdd 
	* @Description: TODO(索赔工时新增:旧的方法) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void claimLaborAddOLD(){
		try {
			String errorMsg = null;
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(loginUser);
			String laborCode = request.getParamValue("LABOR_CODE");  //索赔工时代码
			String laborName = request.getParamValue("LABOR_NAME");  //索赔工时名称
			String wrgroupId = request.getParamValue("WRGROUP_ID");  //索赔车型组ID
			String firstClass = request.getParamValue("FIRST_CLASS");//大类
			String secondClass = request.getParamValue("SECOND_CLASS");//小类
			float labourQuotiety = Utility.getFloat(request.getParamValue("LABOR_QUOTIETY")) ;//工时系数
			float labourHour = Utility.getFloat(request.getParamValue("LABOUR_HOUR"));//索赔工时
			String firstLetter = String.valueOf(laborCode.charAt(0));//大类代码
			String firstTwoLetter = laborCode.substring(0,2);//小类代码
			//待增加的po
			TtAsWrWrlabinfoPO addpo = new TtAsWrWrlabinfoPO();
			addpo.setWrgroupId(new Long(wrgroupId));
			addpo.setCreateBy(loginUser.getUserId());
			addpo.setCreateDate(new Date());
			
			//判断大类是否存在：
			TtAsWrWrlabinfoPO firstpo = new TtAsWrWrlabinfoPO();
			firstpo.setWrgroupId(new Long(wrgroupId));
			firstpo.setLabourCode(firstLetter);
			firstpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_01);//大类树
			List list = dao.select(firstpo);
			if(list != null && list.size() == 0){
				if(Utility.testString(firstClass)){
					addpo.setCnDes(firstClass);
					addpo.setLabourCode(firstLetter);
					addpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_01);
					addpo.setLabourHour(Float.parseFloat("0"));
					addpo.setLabourQuotiety(Float.parseFloat("0"));
					addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
					dao.insert(addpo);
				}else{
					errorMsg = "F";//大类名称没有
				}
			}
			if(errorMsg == null){
				TtAsWrWrlabinfoPO secondpo = new TtAsWrWrlabinfoPO();
				secondpo.setWrgroupId(new Long(wrgroupId));
				secondpo.setLabourCode(firstTwoLetter);
				secondpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_02);//小类树
				List seclist = dao.select(secondpo);
				if(seclist != null && seclist.size() == 0){
					if(Utility.testString(secondClass)){
						addpo.setCnDes(secondClass);
						addpo.setLabourCode(firstTwoLetter);
						addpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_02);
						addpo.setLabourHour(Float.parseFloat("0"));
						addpo.setLabourQuotiety(Float.parseFloat("0"));
						addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
						dao.insert(addpo);
					}else{
						errorMsg = "S";//小类名称没有
					}
				}				
				
			}
			if(errorMsg == null){
				TtAsWrWrlabinfoPO selpo = new TtAsWrWrlabinfoPO();
				selpo.setWrgroupId(new Long(wrgroupId));
				selpo.setLabourCode(laborCode);
//				selpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_03);//小类树
				List sellist = dao.select(selpo);
				if(sellist != null && sellist.size() == 0){
					addpo.setCnDes(laborName);
					addpo.setLabourCode(laborCode);
					addpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_03);
					addpo.setLabourHour(labourHour);
					addpo.setLabourQuotiety(labourQuotiety);
					addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
					dao.insert(addpo);
				}else{
					errorMsg = laborCode;//工时代码已经存在
				}
			}

			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setOutData("error", errorMsg);
			act.setOutData("success", "true");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborAdd 
	* @Description: TODO(索赔工时新增) 
	* @param    
	* @return void  
	* @throws
	* modify at 2010-07-15
	 */
	@SuppressWarnings("unchecked")
	public void claimLaborAdd(){
		
		
		try {
			
			String errorMsg = null;
			String laborCode = request.getParamValue("LABOR_CODE");  //索赔工时代码
			String laborName = request.getParamValue("LABOR_NAME");  //索赔工时名称
			String wrgroupId = request.getParamValue("WRGROUP_ID");  //索赔车型组ID
			String paterId = request.getParamValue("P_ID");//工时大类
			float labourQuotiety = Utility.getFloat(request.getParamValue("LABOR_QUOTIETY")) ;//工时系数
			float labourHour = Utility.getFloat(request.getParamValue("LABOUR_HOUR"));//索赔工时
			
			//zhumingwei 2011-08-10
			String remark = request.getParamValue("remark");//工时大类
			
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(loginUser);
			//待增加的po
			String[] ids = wrgroupId.split(",");
			for(int i=0;i<ids.length;i++){
				
			
				TtAsWrWrlabinfoPO addpo = new TtAsWrWrlabinfoPO();
				addpo.setWrgroupId(new Long(ids[i]));
				addpo.setCreateBy(loginUser.getUserId());
				addpo.setCreateDate(new Date());
				
				//zhumingwei 2011-08-10
				addpo.setRemark(remark);
					if(errorMsg == null){
						TtAsWrWrlabinfoPO selpo = new TtAsWrWrlabinfoPO();
						selpo.setWrgroupId(new Long(ids[i]));
						selpo.setLabourCode(laborCode);
						List sellist = dao.select(selpo);
						if(sellist != null && sellist.size() == 0){
							addpo.setCnDes(laborName);
							addpo.setLabourCode(laborCode);
							addpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_03);
							addpo.setLabourHour(labourHour);
							addpo.setLabourQuotiety(labourQuotiety);
							addpo.setPaterId(Utility.getLong(paterId));//大类
							addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
							addpo.setOemCompanyId(oemCompanyId);
							dao.insert(addpo);
						}else{
							errorMsg = laborCode;//工时代码已经存在
						}
					}
			}

			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setOutData("error", errorMsg);
			act.setOutData("success", "true");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}	
	/**
	 * 
	* @Title: AdditionalLaborAddInit 
	* @Description: TODO(附加工时新增初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void additionalLaborAddInit(){
		
		
		try {
			
			String wId = request.getParamValue("ID");//工时id
			String wrgroupId = request.getParamValue("WRGROUP_ID");//车型组
			act.setOutData("W_ID", wId);
			act.setOutData("WRGROUP_ID", wrgroupId);
			act.setForword(ADDITIONAL_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: AdditionalLaborAdd 
	* @Description: TODO(附加工时新增) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void additionalLaborAdd(){
		
		
		Long oemCompanyId=GetOemcompanyId.getOemCompanyId(loginUser); 
		String errorMsg = null;
		try {
			
			String wId = request.getParamValue("W_ID");//工时id
			String wrgroupId = request.getParamValue("WRGROUP_ID");//车型组
			String labourCode = request.getParamValue("ADD_LABOUR_CODE");//附加工时代码
			String cnDes = request.getParamValue("ADD_CN_DES");//附加工时名称
			float labourQuotiety = Utility.getFloat(request.getParamValue("ADD_LABOUR_QUOTIETY"));//工时系数
			float labourHour = Utility.getFloat(request.getParamValue("ADD_LABOUR_HOUR"));//索赔工时
				TtAsWrWrlabinfoPO selpo = new TtAsWrWrlabinfoPO();
				selpo.setWrgroupId(new Long(wrgroupId));
				selpo.setLabourCode(labourCode);
				List sellist = dao.select(selpo);
				if(sellist != null && sellist.size() == 0){
					//索赔工时表增加一条记录：
					TtAsWrWrlabinfoPO addpo = new TtAsWrWrlabinfoPO();
					addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
					addpo.setCreateBy(loginUser.getUserId());
					addpo.setCreateDate(new Date());
					addpo.setTreeCode(Constant.CLAIM_LABHOUR_TREE_CODE_04);//附加工时层：4
					addpo.setCnDes(cnDes);
					addpo.setLabourCode(labourCode);
					addpo.setLabourHour(labourHour);
					addpo.setLabourQuotiety(labourQuotiety);
					addpo.setWrgroupId(new Long(wrgroupId));
					addpo.setOemCompanyId(oemCompanyId);
					dao.insert(addpo);
					//索赔工时附加工时关系表增加一条记录：
					TtAsWrAdditionalitemPO additempo = new TtAsWrAdditionalitemPO();
					additempo.setId(Long.parseLong(SequenceManager.getSequence("")));
					additempo.setAddId(addpo.getId());//附加工时id
					additempo.setWId(new Long(wId));//索赔工时id
					additempo.setCreateBy(loginUser.getUserId());
					additempo.setCreateDate(new Date());
					dao.insert(additempo);
					additionalLaborReturn(wId);
				}else{
					errorMsg = labourCode;//工时代码已经存在
				}
				act.setOutData("error", errorMsg);
				act.setOutData("success", "true");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: additionalLaborUpdateInit 
	* @Description: TODO(附加工时修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void additionalLaborUpdateInit(){
		
		
		try {
			
			String id=request.getParamValue("ID");
			String wId = request.getParamValue("W_ID");//索赔工时id
			HashMap hm = null;
			hm = dao.getAdddClaimLaborById(new Long(id));
			
			act.setOutData("SELMAP", hm);//对应的索赔工时
			act.setOutData("WID", wId);
			act.setForword(ADDITIONAL_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: additionalLaborUpdate 
	* @Description: TODO(附加工时修改) 
	* @param    
	* @return void  
	* @throws
	 */
	public void additionalLaborUpdate(){
		
		
		TtAsWrWrlabinfoPO selpo = null;
		TtAsWrWrlabinfoPO updatepo = null;
		try {
			
			String id=request.getParamValue("ID");
			String wId = request.getParamValue("W_ID");//工时id
			float labourQuotiety = Utility.getFloat(request.getParamValue("ADD_LABOUR_QUOTIETY"));//工时系数
			float labourHour = Utility.getFloat(request.getParamValue("ADD_LABOUR_HOUR"));//索赔工时
			selpo = new TtAsWrWrlabinfoPO();
			selpo.setId(new Long(id));
			updatepo = new TtAsWrWrlabinfoPO();
			updatepo.setLabourQuotiety(labourQuotiety);
			updatepo.setLabourHour(labourHour);
			updatepo.setUpdateBy(loginUser.getUserId());
			updatepo.setUpdateDate(new Date());
			dao.laborUpdate(selpo, updatepo);
			additionalLaborReturn(wId);

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * 
	* @Title: additionalLaborReturn 
	* @Description: TODO(附加工时新增回显) 
	* @param @param id   
	* @return void  
	* @throws
	 */
	private void additionalLaborReturn(String id){
		try {
			HashMap hm = null;
			hm = dao.getClaimLaborById(new Long(id));
			List addlist =  dao.getAddLabourById(new Long(id));
			List businessCodeList = dao.getBussCodeById(new Long(id));
			act.setOutData("SELMAP", hm);//对应的索赔工时
			act.setOutData("ADDLIST", addlist);//索赔工时对应的附加工时			
			act.setOutData("BUSCODELIST", businessCodeList);//索赔工时对应的故障代码
			act.setForword(CLAIM_LABOR_UPDATE_URL);			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
	}
	
	public void claimLaborMultiUpdateInit(){
		
		
		try {
			
			String arrId = request.getParamValue("ARRID");
			HashMap hm = null;
			HashMap<String,Object> arrTwoId = new HashMap<String,Object>();
			String[] arrList = arrId.split(",");
			for (int i=0;i<arrList.length;i++) {
				hm = dao.getClaimLaborById(new Long(arrList[i]));
				arrTwoId.put(arrList[i], hm.get("WRGROUP_ID"));
			}
//			List addlist =  dao.getAddLabourById(new Long(id));
//			List businessCodeList = dao.getBussCodeById(new Long(id));
			
			act.setOutData("SELMAP", hm);//对应的索赔工时
//			act.setOutData("ADDLIST", addlist);//索赔工时对应的附加工时
//			act.setOutData("BUSCODELIST", businessCodeList);//索赔工时对应的故障代码
			act.setOutData("arrTwoId", arrTwoId);
			act.setForword(CLAIM_LABOR_UPDATE_URL2);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: additionalLaborDel 
	* @Description: TODO(附加工时删除) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void additionalLaborDel(){
		
		
		TtAsWrWrlabinfoPO selpo = null;
		TtAsWrWrlabinfoPO updatepo = null;
//		TtAsWrAdditionalitemPO addSelpo = null;
//		TtAsWrAdditionalitemPO addUpdatepo = null;
		TtAsWrAdditionalitemPO delpo = null;
		try {
			
			String id = request.getParamValue("ID");  //关系表ID
			String addId = request.getParamValue("ADD_ID");//附加工时ID
//			String wId = request.getParamValue("W_ID");//索赔工时ID
			selpo = new TtAsWrWrlabinfoPO();
			selpo.setId(new Long(addId));
			updatepo = new TtAsWrWrlabinfoPO();
			updatepo.setIsDel(Constant.IS_DEL);//删除标记
			updatepo.setUpdateBy(loginUser.getUserId());
			updatepo.setUpdateDate(new Date());
			dao.laborUpdate(selpo, updatepo);//删除索赔工时表
			
//			addSelpo = new TtAsWrAdditionalitemPO();
//			addUpdatepo = new TtAsWrAdditionalitemPO();
//			addSelpo.setAddId(new Long(id));
//			addSelpo.setWId(new Long(wId));
//			addUpdatepo.setIsDel(Constant.IS_DEL);//删除标记
//			addUpdatepo.setUpdateBy(loginUser.getUserId());
//			addUpdatepo.setUpdateDate(new Date());
//			dao.laborUpdate(addSelpo, addUpdatepo);//删除索赔工时附加工时关系表
			delpo = new TtAsWrAdditionalitemPO();
			delpo.setId(new Long(id));
			dao.delete(delpo);
			act.setOutData("success", "true");

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: businessCodeDel 
	* @Description: TODO(故障代码关联删除) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void businessCodeDel(){
		
		
		TtAsWrTrobleMapPO delpo = null;
		try {
			
			String id = request.getParamValue("ID");  //索赔工时与故障代码对应ID
			delpo = new TtAsWrTrobleMapPO();
			delpo.setMapId(new Long(id));
			dao.delete(delpo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}	
	/**
	 * 
	* @Title: claimLaborUpdateInit 
	* @Description: TODO(索赔工时修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborUpdateInit(){
		
		
		try {
			
			String id=request.getParamValue("ID");
			HashMap hm = null;
			hm = dao.getClaimLaborById(new Long(id));
			List addlist =  dao.getAddLabourById(new Long(id));
			List businessCodeList = dao.getBussCodeById(new Long(id));
			
			act.setOutData("SELMAP", hm);//对应的索赔工时
			act.setOutData("ADDLIST", addlist);//索赔工时对应的附加工时
			act.setOutData("BUSCODELIST", businessCodeList);//索赔工时对应的故障代码
			act.setForword(CLAIM_LABOR_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborUpdate 
	* @Description: TODO(索赔工时修改) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborUpdate(){
		
		
		TtAsWrWrlabinfoPO selpo = null;
		TtAsWrWrlabinfoPO updatepo = null;
		try {
			
			String id=request.getParamValue("ID");
			String CN_DES=request.getParamValue("CN_DES");//工时名称
			String wrgroupId=request.getParamValue("WRGROUP_ID");
			float labourQuotiety = Utility.getFloat(request.getParamValue("LABOUR_QUOTIETY"));//工时系数
			float labourHour = Utility.getFloat(request.getParamValue("LABOUR_HOUR"));//索赔工时
			//zhumingwei 2011-8-10
			String remark=request.getParamValue("remark");
			if(remark==null ){
				remark = " ";
			}
			//zhumingwei 2011-8-10
			//modify at 2010-07-28 start
			//增加索赔工时大类
			//String pLabourCode=request.getParamValue("P_LABOUR_CODE");//工时大类代码
			String pId=request.getParamValue("P_ID");//工时大类ID
			//modify at 2010-07-28 end
			selpo = new TtAsWrWrlabinfoPO();
			selpo.setId(new Long(id));
			selpo.setWrgroupId(new Long(wrgroupId));
			updatepo = new TtAsWrWrlabinfoPO();
			updatepo.setLabourQuotiety(labourQuotiety);
			updatepo.setLabourHour(labourHour);
			updatepo.setPaterId(Utility.getLong(pId));
			updatepo.setIfStatus(0);
			updatepo.setUpdateBy(loginUser.getUserId());
			updatepo.setUpdateDate(new Date());
			updatepo.setCnDes(CN_DES);
			updatepo.setRemark(remark);
			
			String arrTwoId = CommonUtils.checkNull(request.getParamValue("arrTwoId")).replace("{", "").replace("}", "");
			if (Utility.testString(arrTwoId)) {
				String[] addList = arrTwoId.split(",");
				for (int i=0;i<addList.length;i++) {
					selpo.setId(Long.parseLong(addList[i].split("=")[0].trim()));
					selpo.setWrgroupId(Long.parseLong(addList[i].split("=")[1]));
					dao.claimlaborUpdate(selpo, updatepo);
				}
			} else {
				dao.claimlaborUpdate(selpo, updatepo);
			}
			act.setOutData("success", "true");

		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	
	public void batchUpdate(){
		
		
		try {
			
			String wrgroupId = request.getParamValue("WRGROUP_ID");//车型组
			String labourCode = request.getParamValue("LABOUR_CODE");//工时代码
			String cnDes = request.getParamValue("CN_DES");//中文说明(工时名称)
			String labourCodebig = request.getParamValue("LABOUR_CODE_BIG");//工时大类代码
			String cnDesbig = request.getParamValue("CN_DES_BIG");//工时大类名称	
			String quantiy = request.getParamValue("quantity2");
			dao.batchUpdateDao(wrgroupId, labourCode, cnDes, quantiy);
			act.setOutData("success", "true");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时批量更新");
			logger.error(loginUser,e1);
			act.setOutData("success", "false");
			act.setException(e1);
		}		
	}
	/**
	 * 
	* @Title: businessCodeAddInit 
	* @Description: TODO(故障代码新增初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void businessCodeAddInit(){
		
		
		Long companyId=GetOemcompanyId.getOemCompanyId(loginUser);
		try {
			
			String laborId = request.getParamValue("ID");//索赔工时ID
			List busicodelist = dao.getBusinessCodeByType(Constant.BUSINESS_CHNG_CODE_04,laborId,companyId);
			act.setOutData("BUSICODELIST", busicodelist);
			act.setOutData("W_ID", laborId);
			act.setForword(BUSINESS_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: businessCodeAdd 
	* @Description: TODO(故障代码新增) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void businessCodeAdd(){
		
		
		try {
			
			String laborId = request.getParamValue("W_ID");//索赔工时ID
			String[] troubleIdArray = request.getParamValues("businesscodeIds");//要添加的故障代码id
//			String troubleIds = request.getParamValue("businesscodeIds");//要添加的故障代码id，以,隔开
				for (int i = 0;i<troubleIdArray.length;i++) {
					TtAsWrTrobleMapPO tawtmpo = new TtAsWrTrobleMapPO();
					tawtmpo.setMapId(Long.parseLong(SequenceManager.getSequence("")));//id
					tawtmpo.setLaborId(new Long(laborId));
					tawtmpo.setTroubleId(new Long(troubleIdArray[i]));
					dao.insert(tawtmpo);
				}
			additionalLaborReturn(laborId);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborDel 
	* @Description: TODO(索赔工时删除) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborDel(){
		
		
		TtAsWrWrlabinfoPO delpo = null;
		TtAsWrWrlabinfoPO selpo = null;
		try {
			
			String id = request.getParamValue("ID");  //索赔工时ID
			selpo = new TtAsWrWrlabinfoPO();
			selpo.setId(new Long(id));
			delpo = new TtAsWrWrlabinfoPO();
			delpo.setIsDel(Constant.IS_DEL);
			delpo.setUpdateBy(loginUser.getUserId());
			delpo.setUpdateDate(new Date());
			dao.deleteClaimlabor(new Long(id), selpo, delpo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimLaborDetail 
	* @Description: TODO(索赔工时详细信息) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborDetail() {
		
		
		try {
			
			String id=request.getParamValue("ID");
			HashMap hm = null;
			hm = dao.getClaimLaborById(new Long(id));
			List addlist =  dao.getAddLabourById(new Long(id));
			List businessCodeList = dao.getBussCodeById(new Long(id));
			
			act.setOutData("SELMAP", hm);//对应的索赔工时
			act.setOutData("ADDLIST", addlist);//索赔工时对应的附加工时
			act.setOutData("BUSCODELIST", businessCodeList);//索赔工时对应的故障代码
			act.setForword(CLAIM_LABOR_DETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-9-28
	public void partThreeBagsQueryInit(){
		
		
		String dealerId = loginUser.getDealerId();
		boolean isDealer = false;//是否经销商用户
		if(dealerId!=null && dealerId!=""){
			isDealer = true;
		}
		act.setOutData("isDealer", isDealer);
		try {
			act.setForword(PART_THREE_BAGS_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-9-28
	public void partThreeBagsQuery11() {
		
		
		Long oemCompanyId=GetOemcompanyId.getOemCompanyId(loginUser);
		StringBuffer sb = new StringBuffer();
		try {
			
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//代码列别
				String partCode = request.getParamValue("partCode");
				String partName = request.getParamValue("partName");
				String threeCode = request.getParamValue("threeCode");
				String threeName = request.getParamValue("threeName");
				//拼sql的查询条件
				if (Utility.testString(partCode)) {
					sb.append(" and a.part_code like '%"+partCode+"%'");
				}
				if (Utility.testString(partName)) {
					sb.append(" and a.part_name like '%"+partName+"%'");
				}
				if (Utility.testString(threeCode)) {
					sb.append(" and b.rule_code like '%"+threeCode+"%'");
				}
				if (Utility.testString(threeName)) {
					sb.append(" and b.rule_name like '%"+threeName+"%'");
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.partThreeBagsQuery11(Constant.PAGE_SIZE, curPage,oemCompanyId,sb.toString());
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(loginUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-9-28
	public void partThreeBagsDetailInit(){ 
		String ruleId=request.getParamValue("ID");
		request.setAttribute("ruleId", ruleId);
		act.setForword(PART_THREE_BAGS_DETAIL_URL);
	}
	//zhumingwei 2011-9-28
	public void partThreeBagsDetail(){ 
		String ruleId=request.getParamValue("ruleId");
		try {
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.partThreeBagsDetail(ruleId,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(PART_THREE_BAGS_DETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
}
	/**
	 * 
	* @Title: claimLaborQuery 
	* @Description: TODO(索赔工时查询) zyw 2015-4-14重构
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborQueryExport() {
		try {
			act.getResponse().setContentType("application/json");
			PageResult<Map<String, Object>> ps=dao.claimLaborQueryData(request,Constant.PAGE_SIZE_MAX, getCurrPage());
			dao.claimLaborQueryToExecl(act,ps);
			act.setForword(CLAIM_LABOR_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
}
