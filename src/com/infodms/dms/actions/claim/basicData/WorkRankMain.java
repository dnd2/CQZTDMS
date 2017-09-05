package com.infodms.dms.actions.claim.basicData;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.basicData.WorkRankDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrWarehousePO;
import com.infodms.dms.po.TtPtWarehouseRecardPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OBJ14;
import com.infoservice.filestore.FileStore;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

 

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
/**
 * 
* @ClassName: WorkRankMain 
* @Description: TODO() 
* @author luole 
* @date 2013-1-16 上午10:53:58 
*
 */
public class WorkRankMain {
	private Logger logger = Logger.getLogger(ClaimLaborMain.class);
	private final WorkRankDao dao = WorkRankDao.getInstance();
	private final String WORK_RANK_URL="/jsp/claim/basicData/workRankIndex.jsp";//工作等级维护查询主页
	private final String SHOW_ADD_WORK_RANK_URL="/jsp/claim/basicData/showAddWorkRank.jsp";//新增功能页面
	private final String LABOUR_CODE_URL="/jsp/claim/basicData/showLabourCode.jsp";//查询工时代码与工时名称弹出层的页面
	private final String initialInventory="/jsp/claim/basicData/initialInventory.jsp";//初始化库存
	private final String sucJsp="/jsp/claim/basicData/sucJsp.jsp";//初始化库存
	private final String MEND_ENTER_URL = "/jsp/claim/basicData/mendEnterPart.jsp";//补录
	private final String PART_CODE_URL="/jsp/claim/basicData/showPartBase.jsp";//查询配件代码弹出框
	 	/**
	 * 
	* @Title: workRankInit 
	* @Description: TODO(工作等级维护初始化) 
	* @param 
	* @return void
	* @throws
	 */
	public void workRankInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("initWorkRanklist", getWorkRankObject("1", "1357"));
			act.setForword(WORK_RANK_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void mendEnterInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(MEND_ENTER_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: getWorkRankObject 
	* @Description: TODO(获得一个Object类型的集合，主要用于前端下拉框) 
	* @param @param type        ：是否加入"请选择"：“1”(加入)
	* @param @param rankType    ：是否作业等级类型
	* @param @return  
	* @return Object
	* @throws
	 */
	private String getWorkRankObject(String type,String rankType){
		List list = dao.getWorkRankNameList(rankType);  //获得列表
		String str ="";
		if("1".equals(type)){
			str+="<option value=\'\'>-请选择-</option>";
		}
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,Object> map = (Map<String,Object>)list.get(i);
				str+="<option value=\'"+map.get("CODE_ID")+"\'>"+map.get("CODE_DESC")+"</option>";
			}
		}
		return str;
	}
	/**
	 * 
	* @Title: workRankQurey 
	* @Description: TODO(工作等级维护查询) 
	* @param   
	* @return void
	* @throws  
	* @date 2013-01-16
	 */
	public void workRankQurey(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper req = act.getRequest();
			act.getResponse().setContentType("application/json");//设置返回类型
			if("1".equals(req.getParamValue("COMMAND"))){
				//获得上查询条件列表
				String labourCode = req.getParamValue("LABOUR_CODE");
				String labourName = req.getParamValue("LABOUR_NAME");
				String workRankId = req.getParamValue("WORKRANK_ID");
				
				if(labourCode!=null && !labourCode.equals("")){//工时代码 不区分大小写
					sb.append("and td.LABOUR_CODE like '%"+labourCode.toUpperCase()+"%'");
				}
				if(labourName!=null && !labourName.equals("")){
					sb.append("and td.LABOUR_NAME like '%"+labourCode+"%'");
				}
				if(workRankId!=null && !workRankId.equals("")){
					sb.append("and td.DEALER_LABOUR_TYPE = "+workRankId);
				}
				Integer curPage = req.getParamValue("curPage")!=null?Integer.parseInt(req.getParamValue("curPage")):1;
				PageResult<Map<String, Object>> ps = dao.workRankQurey(Constant.PAGE_SIZE, curPage, sb.toString());
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void saveMendPart(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper req = act.getRequest();
		try {
			String partCode[] = req.getParamValues("PART_CODE");
			String partName[] = req.getParamValues("PART_NAME");
			String partNo[] = req.getParamValues("PART_NO");
			Long userId = Long.parseLong(logonUser.getDealerId());
			int sun = 0;
			boolean flag = true;
			String  msg = "";
			int success = 0;
			//先进行配件验证。看此配件是否已添加了库存
			for(int i=0;i<partCode.length;i++){
				if(Utility.testString(partCode[i])){
					if(!dao.checkPart(partCode[i],userId)){
						flag = false;
						msg=msg+(i+1)+"、";
					}
				}
			}
			if(flag){
				//如果都没有在库存中有，那么可以添加
				TtAsWrWarehousePO temptawp = new TtAsWrWarehousePO();
				temptawp.setDealerId(userId);
				TtAsWrWarehousePO tawp = (TtAsWrWarehousePO) dao.select(temptawp).get(0);
				for(int i=0;i<partCode.length;i++){
					if(Utility.testString(partCode[i])){
						/*TtAsWrWarehouseDetailPO tawwdpo = new TtAsWrWarehouseDetailPO();
						tawwdpo.setId(Long.parseLong(SequenceManager.getSequence("")));
						tawwdpo.setCreateBy(userId);
						tawwdpo.setCreateDate(new Date());
						tawwdpo.setPartCode(partCode[i]);
						tawwdpo.setPartName(partName[i]);
						tawwdpo.setQuantity(Double.parseDouble(partNo[i]));
						tawwdpo.setWarehouseId(tawp.getWarehouseId());
						dao.insert(tawwdpo);*/
						sun++;
						//修改2013-01-08 配件补录时让流水去处理
						TtPtWarehouseRecardPO tpwrpo = new TtPtWarehouseRecardPO();
						tpwrpo.setFlowId(Long.parseLong(SequenceManager.getSequence("")));
						tpwrpo.setCreateBy(userId);
						tpwrpo.setCreateDate(new Date());
						tpwrpo.setEntityCode(tawp.getDealerCode());
						tpwrpo.setTopWarehouseCode(tawp.getWarehouseCode());
						tpwrpo.setDmsWarehouseCode(tawp.getTopupWarehouseCode());
						tpwrpo.setPartName(partName[i]);
						tpwrpo.setPartNo(partCode[i]);
						tpwrpo.setInOutType(Constant.Warehousing_library_type_1);
						tpwrpo.setInOutTag(Constant.is_Warehousing_2);
						tpwrpo.setStockInQuantity(Float.parseFloat(partNo[i]));
						tpwrpo.setStockOutQuantity(0f);
						tpwrpo.setStockQuantity(Float.parseFloat(partNo[i]));
						tpwrpo.setOperateDate(new Date());
						tpwrpo.setDataSource(Constant.OEM_WAREHOUSE_SOURCE_TYPE_SHOP);//补录的修改成从店面系统过来的 2014-01-13
						tpwrpo.setDmsSuccess(0);
						//tpwrpo.setDmsDate(new Date());
						tpwrpo.setTopSuccess(0);
						tpwrpo.setWjFlag(dao.checkDataWJ());
						dao.insert(tpwrpo);
					}
				}
				success = 1;
				msg = "成功添加"+sun+"条";
			}else{
				success = 0;
				msg = "第"+msg+"条的配件信息在库存中已存在，如果想改库存，请去鼎腾系统中做变更申请";
			}
			act.setOutData("success", success);
			act.setOutData("msg", msg);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "补录库存");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: showAddWorkRank 
	* @Description: TODO(新增页面初始化) 
	* @param 
	* @return void
	* @throws
	 */
	public void showAddWorkRank(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("workRankAddInit", dao.getWorkRankNameList("1357"));
			act.setForword(SHOW_ADD_WORK_RANK_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: labourCodeInit 
	* @Description: TODO(查询工时代码页面初始化) 
	* @param 
	* @return void
	* @throws
	 */
	public void labourCodeInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(LABOUR_CODE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void partCodeInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_CODE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: laborListQuery 
	* @Description: TODO(工时代码列表查询) 
	* @param 
	* @return void
	* @throws
	 */
	public void laborListQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper req =  act.getRequest();
			String labourCode =req.getParamValue("LABOUR_CODE");
			String labourName = req.getParamValue("LABOUR_NAME");
			if(labourCode!=null && !"".equals(labourCode)){
				sb.append("tt.LABOUR_CODE ='"+labourCode+"' ");
			}
			if(labourName!=null && !"".equals(labourName)){
				if(!sb.equals("")){
					sb.append("and ");
				}
				sb.append("tt.CN_DES ='"+labourName+"' ");
			}
			Integer curPage = req.getParamValue("curPage")!=null?Integer.parseInt(req.getParamValue("curPage")):1;
			PageResult<Map<String, Object>> ps = dao.laborListQuery(Constant.PAGE_SIZE, curPage, sb.toString());
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: 配件代码选择查询
	* @Description: 
	* @param 
	* @date 2013-11-1下午1:50:12
	* @throws luole
	*
	 */
	public void partListQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper req =  act.getRequest();
			String partCode = CommonUtils.checkNull(req.getParamValue("PART_CODE"));
			String partName = CommonUtils.checkNull(req.getParamValue("PART_NAME"));
			Integer curPage = req.getParamValue("curPage")!=null?Integer.parseInt(req.getParamValue("curPage")):1;
			PageResult<Map<String, Object>> ps = dao.partListQuery(Constant.PAGE_SIZE, curPage, partCode,partName);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: addWorkRank 
	* @Description: TODO(新增工作等级维护，如果以有相应的工作等级了，则修改) 
	* @param 
	* @return void
	* @throws
	 */
	public void addWorkRank(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper req = act.getRequest();
			String labourCode = req.getParamValue("LABOUR_CODE");
			String labourName = req.getParamValue("LABOUR_NAME");
			String workRank = req.getParamValue("WORK_RANK");
			String[] wrs = workRank.split(",");
			List<String> wrList = new ArrayList<String>();
			for(String wr : wrs){
				wrList.add(wr);
			}
			/**
			 * 备注，在这儿插入的时候，要去修改数据库中字段的长度  
			 * 
			 */
			if(dao.addWorkRank(labourCode, labourName, wrList,logonUser.getUserId())){
				act.setOutData("success", "true");
			}else{
				act.setOutData("success", "false");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void initialInventory(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			
			String value=dao.queryIshandle(logonUser.getDealerId());
			String daoru = "2";
			String handleDate = "";
			if(Utility.testString(value)){
				handleDate = value;
				daoru = "1";
			}
			String sysdate = CommonUtils.printDate(new Date());
			act.setOutData("handleDate", handleDate);
			act.setOutData("sysdate", sysdate);
			act.setOutData("daoru", daoru);
			act.setForword(initialInventory);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void initialInventoryInto(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			RequestWrapper req = act.getRequest();
			 
			FileObject file = req.getParamObject("carkdVin");
			String fileName = file.getFileName();//获取文件名
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
			ByteArrayInputStream is = new ByteArrayInputStream(file.getContent());//获取文件数据
			System.out.println(fileName);
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			int totalRows = sheet.getRows();
			
			 
			  
		String Id=	SequenceManager.getSequence("");
			  
		dao.initialInventoryInto(Id,logonUser.getDealerId());
			
			for(int i=1;i<totalRows;i++){
				
			 
				Cell[] testcell  = sheet.getRow(i);

				  String tempString  = testcell[0].getContents();
				 
					  
					                      String str1 = testcell[0].getContents();
					                      System.out.println(str1);
					                      String str2 = testcell[1].getContents();
					                      System.out.println(str2);
				 dao.initialInventoryIntoDetail(Id, str1, str2);
				
			}
			 
			 //开通店面系统流水上传，以及OEM与其他库房之间调拨功能
			 TtAsWrWarehousePO po = new TtAsWrWarehousePO();
			 TtAsWrWarehousePO po1 = new TtAsWrWarehousePO();
			 po.setDealerId(Long.valueOf(logonUser.getDealerId()));
			 po1.setIsRechard(Constant.is_warehouse_1);
			 dao.update(po, po1);
			 OBJ14 obj = new OBJ14();
			 obj.execute(logonUser.getDealerCode());
			 
			 TtAsWrWarehousePO housePo = new TtAsWrWarehousePO();
			 TtAsWrWarehousePO housePo1 = new TtAsWrWarehousePO();
			 housePo.setDealerId(Long.valueOf(logonUser.getDealerId()));
			 housePo1.setStatus(1);
			 dao.update(housePo, housePo1);
			act.setOutData("suc", "suc");
			act.setForword(sucJsp);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "工作等级维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
