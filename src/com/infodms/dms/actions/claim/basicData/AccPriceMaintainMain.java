/**   
* @Title: LaborPriceMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(工时单价维护Action) 
* @author zhumingwei   
* @date 2011-6-30 上午11:28:38 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.basicData.AccPriceMainDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAccessoryPriceMaintainPO;
import com.infodms.dms.po.TtAsWrLabourPricePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimLaborMain 
 * @Description: (辅料费维护Action) 
 * @author Administrator 
 * @date 2010-6-1 上午11:05:38 
 *  
 */
public class AccPriceMaintainMain extends BaseImport {
	private Logger logger = Logger.getLogger(ClaimLaborMain.class);
	private final AccPriceMainDao dao = AccPriceMainDao.getInstance();
	private final String ACCESSORY_PRICE_MAINTAIN = "/jsp/claim/basicData/accessoryPriceMaintainIndex.jsp";//主页面（查询）
	private final String ACCESSORY_PRICE_MAINTAIN_ADD = "/jsp/claim/basicData/accessoryPriceMaintainAdd.jsp";//新增页面
	private final String INPORT_PER = "/jsp/claim/basicData/importPer2.jsp";
	private  final String INPUT_ERROR_URL= "/jsp/claim/basicData/inputerror.jsp";//数据导入出错页面
	private  final String INPORT_SURE = "/jsp/claim/basicData/importSureDo2.jsp";//数据导入确认页面

	
	
	
	/**
	 * 
	* @Title:  
	* @Description: (辅料费维护初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void accessoryPriceMaintainInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(ACCESSORY_PRICE_MAINTAIN);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"辅料费维护初始化出错");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void dateQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			String workhour_code = request.getParamValue("workhour_code");
			String workhour_name = request.getParamValue("workhour_name");
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.laborPriceQuery(Constant.PAGE_SIZE, curPage,workhour_code,workhour_name);
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 批量导入跳转
	 */
	public void inportPer(){
		ActionContext act = ActionContext.getContext() ;
		AclUserBean user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(INPORT_PER);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/**
	 * 批量导入模板下载
	 */
	@SuppressWarnings("unchecked")
	public void download(){
		ActionContext act = ActionContext.getContext() ;
		AclUserBean user =(AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			List<List<Object>> list = new ArrayList<List<Object>>();
			String[] listHead = new String[4];
			listHead[0]="工时代码";
			listHead[1]="工时名称";
			listHead[2]="金额";
			listHead[3]="状态";
			
			List list1=new ArrayList();
			String[]detail=new String[4];
			detail[0]="92012100201";
			detail[1]="添加、补充防冻液（1.5桶）";
			detail[2]="99";
			detail[3]="有效";
			list1.add(detail);
	    	com.infodms.dms.actions.claim.basicData.ToExcel.toExceUtil(ActionContext.getContext().getResponse(), request, listHead, list1,"辅料费维护.xls");
	   /**
	    *  以下 是 只有表头，木有示例 数据的生成方法
	    */

		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔工时单价设定");
			logger.error(user, e1);
			act.setException(e1);
		}
	}
	
	public void excelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
		try {
			List<Map<String,String>> errorInfo= new ArrayList<Map<String,String>>();
			RequestWrapper request = act.getRequest();
			long maxSize=1024*1024*5;
			int errNum = insertIntoTmp(request, "uploadFile",4,4,maxSize);
			
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
				List<Map> list=getMapList();
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
			logger.error(user,e1);
			act.setException(e1);
		}
	}
	
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
						errormap.put("2", "工时代码");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						PageResult<Map<String, Object>> dealerList = dao.laborPriceQuery(10,1,cells[0].getContents().trim(),null);
						if (dealerList.getTotalRecords() >= 1) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key+ "行");
							errormap.put("2", "工时代码");
							errormap.put("3", "重复");
							errorInfo.add(errormap);
						} 
					}
					tempmap.put("1", cells[0].getContents().trim());
					if ("".equals(cells[1].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "工时名称");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} 
					
					tempmap.put("2", cells[1].getContents().trim());
					
					if ("".equals(cells[2].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "金额");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						Pattern pattern = Pattern.compile("^[0-9]+([.][0-9]{0,3})?$");//==验证金额lj 2015-6-11 
						if (!pattern.matcher(cells[2].getContents().trim()).matches()) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "金额");
							errormap.put("3", "非法字符");
							errorInfo.add(errormap);
						}
					}
					tempmap.put("3", cells[2].getContents().trim());
					
					if ("".equals(cells[3].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "状态");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					} else {
						if (!("有效").equals(cells[3].getContents().trim()) && !("无效").equals(cells[3].getContents().trim())) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
							errormap.put("2", "状态");
							errormap.put("3", "非法字符");
							errorInfo.add(errormap);
						}
					}
					tempmap.put("4", cells[3].getContents().trim());
					voList.add(tempmap);
			}	
		}
	}
	
	@SuppressWarnings("unchecked")
	public void importAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		try {
			RequestWrapper  request = act.getRequest();
			String strCount = CommonUtils.checkNull(request.getParamValue("count")); //导入数据总条数
			
			int count = Integer.parseInt(strCount);
			for(int i=1;i<count; i++){
				String code = CommonUtils.checkNull(request.getParamValue("code"+i));//车型大类
				String name = CommonUtils.checkNull(request.getParamValue("name"+i));//车型大类
				String price = CommonUtils.checkNull(request.getParamValue("price"+i));//工时单价
				String status = CommonUtils.checkNull(request.getParamValue("status"+i));//经销商ID
				Integer status2 = Constant.STATUS_DISABLE;
				if (("有效").equals(status)) {
					status2 = Constant.STATUS_ENABLE;
				}
				TtAccessoryPriceMaintainPO po = new TtAccessoryPriceMaintainPO() ;
				PageResult<Map<String, Object>> list = dao.laborPriceQuery(10,1,code,null);
				int totalCounts = list.getTotalRecords();
				// 此经销商对应的工时大类不存在则执行添加操作
				if(totalCounts < 1){
					Long logId = Long.parseLong(SequenceManager.getSequence(""));
					po.setId(logId);
					po.setWorkhourCode(code);
					po.setWorkhourName(name);
					po.setPrice(new Double(price));
					po.setAddBy(user.getUserId());
					po.setAddTime(new Date());
					po.setStatus(status2);
					dao.insert(po);
				}else{
					// 此经销商对应的工时大类存在则执行修改操作
//					TtAccessoryPriceMaintainPO po2 = new TtAccessoryPriceMaintainPO() ;
//					po.setId(Long.parseLong((String)list.getRecords().get(0).get("ID")));
//					po2.setUpdateBy(user.getUserId());
//					po2.setUpdateTime(new Date());
//					po2.setWorkhourCode(code);
//					po2.setWorkhourName(name);
//					po2.setPrice(new Double(price));
//					po2.setStatus(status2);
//					dao.update(po, po2);	
				}
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"辅料费维护导入到数据库中");
			logger.error(user,e1);
			act.setException(e1);
		}
}
	
	//新增初始化
	public void addDate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String workhourCodeExist = "";
			String workhourCode = request.getParamValue("workhour_code");
			TtAccessoryPriceMaintainPO po = new TtAccessoryPriceMaintainPO();
			List<TtAccessoryPriceMaintainPO> list = dao.select(po);
			
			
			for (int i = 0; i < list.size(); i++) {
				if(workhourCode.equals(CommonUtils.checkNull(list.get(i).getWorkhourCode()))){
					workhourCodeExist = "工时代码"+workhourCode+"已经存在!";
					break;
				}
			}
			if(!CommonUtils.isEmpty(workhourCodeExist)){
				act.setOutData("workhourCodeExist", workhourCodeExist);
			}else{
				String workhourName = request.getParamValue("workhour_name");
				String price = request.getParamValue("price");
				String status = request.getParamValue("status");
//				if(!CheckUtil.checkFormatNumber1(price)){
//					act.setOutData("priceError", true);
//				}else{
//					TtAccessoryPriceMaintainPO po = new TtAccessoryPriceMaintainPO();
					po.setPrice(Double.valueOf(price));
					po.setWorkhourCode(workhourCode);
					po.setWorkhourName(workhourName);
					po.setAddBy(logonUser.getUserId());
					po.setAddTime(new Date());
					po.setStatus(Integer.parseInt(status));
					
					Long id = Long.parseLong(SequenceManager.getSequence(""));
					po.setId(id);
					dao.insert(po);
		
					act.setOutData("ok", "ok");
//					act.setForword(ACCESSORY_PRICE_MAINTAIN_ADD);
			}
			
//			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"辅料费维护新增失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//新增初始化
	public void addInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(ACCESSORY_PRICE_MAINTAIN_ADD);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"新增初始化失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//修改初始化
	public void modInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			
			String id = request.getParamValue("ID");
			String price = request.getParamValue("price_"+id);
			String status = request.getParamValue("status_"+id);
//			if(CheckUtil.checkFormatNumber1(price)){
				
				TtAccessoryPriceMaintainPO newPo = new TtAccessoryPriceMaintainPO();
				TtAccessoryPriceMaintainPO oldPo = new TtAccessoryPriceMaintainPO();
				
				newPo.setPrice(Double.valueOf(price));
				newPo.setUpdateBy(logonUser.getUserId());
				newPo.setUpdateTime(new Date());
				newPo.setStatus(Integer.parseInt(status));
				
				oldPo.setId(Long.valueOf(id));
				
				dao.update(oldPo,newPo);
				
				act.setOutData("ok", "ok");
//			}else{
//				act.setOutData("priceError", true);
//			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改辅料维护价格失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}