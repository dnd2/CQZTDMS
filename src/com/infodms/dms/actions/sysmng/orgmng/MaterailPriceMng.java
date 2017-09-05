/**********************************************************************
* <pre>
* FILE : DlrInfoMng.java
* CLASS : DlrInfoMng
*
* AUTHOR : LAX
*
* FUNCTION : 经销商信息维护.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-18| LAX  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: DlrInfoMng.java,v 1.6 2010/12/01 03:51:49 yangz Exp $
 */
package com.infodms.dms.actions.sysmng.orgmng;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.storageManage.MaterailPriceMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsPriceDtlPO;
import com.infodms.dms.po.TtVsPriceDtlTempPO;
import com.infodms.dms.po.TtVsPricePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

import jxl.Cell;

/**
 * Function       :  物料价目表
 * @author        :  LAX
 * CreateDate     :  2009-08-18
 * @version       :  0.1
 */
public class MaterailPriceMng extends BaseImport{
	private Logger logger = Logger.getLogger(MaterailPriceMng.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private static final MaterailPriceMngDAO dao = MaterailPriceMngDAO.getInstance();
	
	private  final String  MaterailPriceMnginit =  "/jsp/systemMng/orgMng/materailPriceMngInit.jsp";
	private  final String  MaterailPriceAdd =  "/jsp/systemMng/orgMng/materailPriceAdd.jsp";
	private  final String MaterailPriceDetailUrl = "/jsp/systemMng/orgMng/materailPriceDetail.jsp";
	private final String MaterailPriceDetailFailureUrl = "/jsp/systemMng/orgMng/materailPriceDetailFail.jsp";
	private  final String  MaterailPriceDetailSuccessUrl =  "/jsp/systemMng/orgMng/materailPriceDetailSuccess.jsp";
	private  final String  MaterailPriceImportFinishUrl =  "/jsp/systemMng/orgMng/materailPriceImportFinish.jsp";
	public void materailPriceInit() throws Exception {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logonUser.getDutyType();//判断是否是经销商端还是车厂端
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("logonUser", logonUser);
//			String dealerIds = logonUser.getDealerId();
//			List<Map<String,Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds);
//			act.setOutData("dealerList", dealerList);
			act.setForword(MaterailPriceMnginit);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void materailPriceAdd() throws Exception {	
		try{				
			act.setForword(MaterailPriceAdd);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void materailPriceSave() throws Exception {
		try{
			Long companyId = logonUser.getCompanyId();
			RequestWrapper request = act.getRequest();
			String priceCode = request.getParamValue("priceCode");
			String pirceName = request.getParamValue("pirceName");
			String startDate = request.getParamValue("startDate");
			String endDate = request.getParamValue("endDate");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Long mseq=new Long(SequenceManager.getSequence(""));
			TtVsPricePO price = new TtVsPricePO();
			price.setPriceId(mseq);
			price.setCompanyId(companyId);
			price.setPriceCode(priceCode);
			price.setPriceDesc(pirceName);
			price.setStartDate(sdf.parse(startDate));
			price.setEndDate(sdf.parse(endDate));
			price.setCreateDate(new Date());
			dao.insert(price);
			act.setOutData("success", "成功");
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void materailPriceQuery() throws Exception {
		AclUserBean logonUser = null;
		try{
			RequestWrapper request = act.getRequest();
			String curPage =  CommonUtils.checkNull(request.getParamValue("curPage"));
			String pageSize =  CommonUtils.checkNull(request.getParamValue("pageSize"));
			if(curPage==""){
				curPage = 1+"";
			}
			if(pageSize==""){
				pageSize = Constant.PAGE_SIZE+"";
			}
			
			String priceCode =  CommonUtils.checkNull(request.getParamValue("priceCode"));
			String priceName =  CommonUtils.checkNull(request.getParamValue("priceName"));
			String startDate =  CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate =  CommonUtils.checkNull(request.getParamValue("endDate"));
			String createDate =  CommonUtils.checkNull(request.getParamValue("createDate"));
			Map<String, String> dataPara = new HashMap<String, String>();
			dataPara.put("priceCode",priceCode);
			dataPara.put("priceName",priceName);
			dataPara.put("startDate",startDate);
			dataPara.put("endDate",endDate);
			dataPara.put("createDate",createDate);

			PageResult<Map<String, Object>> ps = dao.getMaterailPriceDetail(dataPara,Integer.parseInt(curPage),Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void materailPriceQueryShow() throws Exception {
		try{
			RequestWrapper request = act.getRequest();
			String PRICE_ID =  CommonUtils.checkNull(request.getParamValue("PRICE_ID"));
			List<Map<String, Object>> priceDeatail = dao.getMaterailPriceById(PRICE_ID);
			act.setOutData("PRICE_ID", PRICE_ID);
			String PRICE_CODE = "";
			String PRICE_DESC = "";
			String START_DATE = "";
			String END_DATE = "";
			String CREATE_DATE = "";
			if(priceDeatail.size()>0){
				 PRICE_CODE = (String) priceDeatail.get(0).get("PRICE_CODE");
				 PRICE_DESC = (String) priceDeatail.get(0).get("PRICE_DESC");
				 START_DATE = (String) priceDeatail.get(0).get("START_DATE");
				 END_DATE = (String) priceDeatail.get(0).get("END_DATE");
				 CREATE_DATE = (String) priceDeatail.get(0).get("CREATE_DATE");
			}
			act.setOutData("PRICE_CODE", PRICE_CODE);
			act.setOutData("PRICE_DESC", PRICE_DESC);
			act.setOutData("START_DATE", START_DATE);
			act.setOutData("END_DATE", END_DATE);
			act.setOutData("CREATE_DATE", CREATE_DATE);
			act.setForword(MaterailPriceDetailUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void materailPriceImportSave() throws Exception{
		try{
			RequestWrapper request = act.getRequest();
			Long companyId = logonUser.getCompanyId();
			String PRICE_ID =  CommonUtils.checkNull(request.getParamValue("PRICE_ID"));
			List<Map<String, Object>> dtlTempList = dao.getTtVsPriceDtlTempDate();
			int listSize = dtlTempList.size();
			TtVsPriceDtlPO priceDtlDel = new TtVsPriceDtlPO();
			//导入新数据删除老数据
			priceDtlDel.setPriceId(Long.parseLong(PRICE_ID));
			dao.delete(priceDtlDel);
			//完成
			for(int i=0;i<listSize;i++){
				Map<String, Object> map= dtlTempList.get(i);
				String groupCode = map.get("GROUP_CODE").toString();
				String salesPrice = map.get("SALES_PRICE").toString();
				String groupId = dao.getGroupID(groupCode);
				Long mseq=new Long(SequenceManager.getSequence(""));
				TtVsPriceDtlPO priceDtl = new TtVsPriceDtlPO();
				priceDtl.setDetailId(mseq);
				priceDtl.setPriceId(Long.parseLong(PRICE_ID));
				priceDtl.setSalesPrice(Double.parseDouble(salesPrice));
				priceDtl.setCreateDate(new Date());
				priceDtl.setCreateBy(companyId);
				priceDtl.setGroupId(Long.parseLong(groupId));
				dao.insert(priceDtl);
			}		
			act.setOutData("PRICE_ID", PRICE_ID);
			act.setForword(MaterailPriceImportFinishUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void materailPriceSuccess() throws Exception{
		try{
			RequestWrapper request = act.getRequest();
			String curPage =  CommonUtils.checkNull(request.getParamValue("curPage"));
			String pageSize =  CommonUtils.checkNull(request.getParamValue("pageSize"));
			if(curPage==""){
				curPage = 1+"";
			}
			if(pageSize==""){
				pageSize = Constant.PAGE_SIZE_MIDDLE+"";
			}
			PageResult<Map<String, Object>> ps = dao.getTtVsPriceDtlTempPageDate(Integer.parseInt(curPage),Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	public void materailPriceQueryDeatailShow() throws Exception {
		try{
			RequestWrapper request = act.getRequest();
			String PRICE_ID =  CommonUtils.checkNull(request.getParamValue("PRICE_ID"));
			String curPage =  CommonUtils.checkNull(request.getParamValue("curPage"));
			String pageSize =  CommonUtils.checkNull(request.getParamValue("pageSize"));
			if(curPage==""){
				curPage = 1+"";
			}
			if(pageSize==""){
				pageSize = Constant.PAGE_SIZE+"";
			}
			PageResult<Map<String, Object>> ps = dao.getMaterailPriceDeatailById(PRICE_ID,Integer.parseInt(curPage),Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void materailPriceDeatailExport() throws Exception{
		OutputStream os = null;
		try{
			RequestWrapper request = act.getRequest();
			ResponseWrapper response = act.getResponse();
			// 导出的文件名
			String fileName = "物料价格表.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			String PRICE_ID =  CommonUtils.checkNull(request.getParamValue("PRICE_ID"));
			List<List<Object>> list = getExportData(PRICE_ID);
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();
		}catch(Exception e){
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "最大配额总量查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private  List<List<Object>> getExportData(String priceId) throws Exception {
		List<List<Object>> list = new LinkedList<List<Object>>();
		List<Object> listTemp = new LinkedList<Object>();
		//listTemp.add("序号");
		listTemp.add("物料编码");
		listTemp.add("物料名称");
		listTemp.add("销售价格");
		list.add(listTemp);
		List<Map<String, Object>> priceList = dao.getMaterailPriceDeatailById(priceId);
		
		for (int i = 0; i < priceList.size(); i++) {
			Map<String, Object> record = priceList.get(i);
			listTemp = new LinkedList<Object>();
			//listTemp.add((i+1)+"");
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_CODE")));
			listTemp.add(CommonUtils.checkNull(record.get("GROUP_NAME")));
			listTemp.add(CommonUtils.checkNull(record.get("SALES_PRICE")));
			list.add(listTemp);
		}
		
		return list;
	}
	
	public void importExport() throws Exception{	
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			
			TtVsPriceDtlTempPO  priceDtltemp = new TtVsPriceDtlTempPO();
			//清空临时表中目标年度的数据
			dao.delete(priceDtltemp);
			int count=4;
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",count,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(MaterailPriceDetailFailureUrl);
			}
			else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmp(list,count);				
				List<ExcelErrors> errorList = checkDate();
				if(errorList.size()>0){
					act.setOutData("errorList", errorList);
					act.setForword(MaterailPriceDetailFailureUrl);
				}
				else{
					String PRICE_ID =  CommonUtils.checkNull(request.getParamValue("PRICE_ID"));
					act.setOutData("PRICE_ID", PRICE_ID);
					act.setForword(MaterailPriceDetailSuccessUrl);
				}
			}
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	private List<ExcelErrors> checkDate(){
		//List<Map<String, Object>> dtlTempList = dao.getTtVsPriceDtlTempDate();		
		List<Map<String, Object>> dtlTempList = dao.getTtVsPriceDtlTempRepeatDate();
		int repeatCodeListSize = dtlTempList.size();	
		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
		for(int i=0;i<repeatCodeListSize;i++){
			Map<String, Object> map= dtlTempList.get(i);
			String groupCode = map.get("GROUP_CODE").toString();
			String lineNumber = (Long.parseLong(map.get("LINE").toString())+1)+"";
			ExcelErrors erros = new ExcelErrors();
			erros.setRowNum(Integer.parseInt(lineNumber));
			erros.setErrorDesc("物料组代码\""+groupCode+"\"重复");
			errorList.add(erros);
		}
		if(repeatCodeListSize>0)//(repeatCodeListSize>0||repeatNameListSize>0){ //先去重
		{	
			return errorList;
		}
		dtlTempList = dao.getTtVsPriceDtlTempDate();
		int listSize = dtlTempList.size();
		for(int i=0;i<listSize;i++){
			Map<String, Object> map= dtlTempList.get(i);
			String groupCode = map.get("GROUP_CODE").toString();
			if(dao.checkGroupCodeIsHave(groupCode)){
				String lineNumber = (Long.parseLong(map.get("LINE").toString())+1)+"";
				ExcelErrors erros = new ExcelErrors();
				erros.setRowNum(Integer.parseInt(lineNumber));
				erros.setErrorDesc("物料组代码\""+groupCode+"\"不成在");
				errorList.add(erros);
			}
			else{
				String salesPrice = map.get("SALES_PRICE").toString();
				//数字是否正确
				String[] split =  salesPrice.split("\\.");
				
				if(split.length>1){
					if(!isNumeric(split[0])||!isNumeric(split[1])){
						ExcelErrors erros = new ExcelErrors();
						erros.setRowNum(i+2);
						erros.setErrorDesc("请填正确的数字且精度为2位");
						errorList.add(erros);	
					}
					else{
						if(split[1].length()>1){
							ExcelErrors erros = new ExcelErrors();
							erros.setRowNum(i+2);
							erros.setErrorDesc("返利金额超出精度");
							errorList.add(erros);
						}
					}
				}
				else{
					if(!isNumeric(salesPrice)){
						ExcelErrors erros = new ExcelErrors();
						erros.setRowNum(i+2);
						erros.setErrorDesc("请填正确的数字且精度为2位");
						errorList.add(erros);	
					}
				}
			}
		}
		return errorList;
	}
	
	private  boolean isNumeric(String str){
	    for (int i = str.length();--i>=0;){  
	       if (!Character.isDigit(str.charAt(i))){
	    	   if(!".".equals(str.charAt(i))){
	    		   return false;
	    	   }
	        }
	    }
	    return true;
}
	
	private void insertTmp(List<Map> list,int count){		
		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
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
				insertTempDoing(cells);				
			}
		}

	}
	
	private void insertTempDoing(Cell[] cells){
		TtVsPriceDtlTempPO  priceDtltemp = new TtVsPriceDtlTempPO();
		//Long mseq=new Long(SequenceManager.getSequence(""));
		//payDetail.setTicketId(mseq);
		//payDetail.setPayDate(new Date());
		priceDtltemp.setGroupCode(cells[1].getContents().trim());
		priceDtltemp.setGroupName(cells[2].getContents().trim());
		priceDtltemp.setSalesPrice(Double.parseDouble(cells[3].getContents().trim()));
		priceDtltemp.setLine(Long.parseLong(cells[0].getRow()+""));
		dao.insert(priceDtltemp);		

	}
}
