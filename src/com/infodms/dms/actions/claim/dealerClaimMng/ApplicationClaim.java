package com.infodms.dms.actions.claim.dealerClaimMng;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.dealerClaimMng.ApplicationClaimDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TtAsServiceOrderPO;
import com.infodms.dms.po.TtAsWrAppOutPO;
import com.infodms.dms.po.TtAsWrAppPartPO;
import com.infodms.dms.po.TtAsWrAppProjectPO;
import com.infodms.dms.po.TtAsWrApplicationClaimPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.afterSales.ServiceOrderDao;
import com.infodms.yxdms.entity.maintain.TtAsEgressPO;
import com.infodms.yxdms.entity.maintain.TtAsWrAuditRecordPO;
import com.infodms.yxdms.entity.maintain.TtAsWrEgressRecordPO;
import com.infodms.yxdms.entity.special.TtAsWrSpeGoodwillClaimPO;
import com.infodms.yxdms.entity.special.TtAsWrSpecialPO;
import com.infodms.yxdms.service.ClaimService;
import com.infodms.yxdms.service.impl.ClaimServiceImpl;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: ClaimBillTrack 
* @Description: TODO(索赔申请单) 
* @author liusw 
 */
public class ApplicationClaim extends BaseAction {
	
	private Logger logger = Logger.getLogger(this.getClass());
	private ActionContext act=ActionContext.getContext();
	private RequestWrapper request=act.getRequest();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private int pageSize=Constant.PAGE_SIZE;//页面显示行数
	private final ApplicationClaimDAO dao = ApplicationClaimDAO.getInstance();
	private ServiceOrderDao sdao=new ServiceOrderDao();
	private ClaimService claimservice=new ClaimServiceImpl();
	//private final String PRINT_PART_URL = "/jsp/claim/dealerClaimMng/printClaimPartLabel.jsp";//配件标签打印
	//private final String BARCODE_PRINT = "/jsp/claim/dealerClaimMng/barcode_Print.jsp";//条码打印查询
	//private final String barcodePrintDoGet= "/jsp/claim/dealerClaimMng/barcode_print_doget.jsp";//条码打印
	private final String CLAIM_LIST_URL = "/jsp/claim/applicationClaim/appClaimList.jsp";// 工单转索赔单主页面（查询）
	private final String CUS_PROBLEM_URL = "/jsp/claim/applicationClaim/cusProblem.jsp";// 转索赔客户问题页面（查询）
	private final String CLAIM_ADD_URL = "/jsp/claim/applicationClaim/appClaimAdd.jsp";// 索赔单新增页面（新增）
	private final String APP_CLAIM_PART_WIN_URL = "/jsp/claim/applicationClaim/appClaimPartWin.jsp";// 选择旧件页面跳转
	private final String APPCLAIM_LIST_URL = "/jsp/claim/applicationClaim/applicationClaimList.jsp";// 索赔单主页面（查询）
	private final String APPCLAIM_FACTORY_ONE_LIST_URL = "/jsp/claim/applicationClaim/applicationClaimFactoryOneList.jsp";// 索赔单审核页面（一级查询）
	private final String APPCLAIM_FACTORY_TWO_LIST_URL = "/jsp/claim/applicationClaim/applicationClaimFactoryTwoList.jsp";// 索赔单审核页面（二级查询）
	private final String APPCLAIM_FACTORY_SE_LIST_URL = "/jsp/claim/applicationClaim/applicationClaimFactorySelectList.jsp";// 索赔单审核页面（查询）
	private final String APPCLAIM_AUDIT_URL = "/jsp/claim/applicationClaim/appClaimAudit.jsp";// 索赔单审核操作页面（查询）
	private final String APPCLAIM_FIND_URL = "/jsp/claim/applicationClaim/appClaimSelect.jsp";// 索赔单详情页面（查询）
	/**
	 * 查询可以生成索赔单的工单
	 */
	public void orderClaim(){
		try{
		//传入参数
		Map<String,Object> paraMap=new HashMap<String,Object>();
		paraMap.put("dealerdId", logonUser.getDealerId());
		paraMap.put("SERVICE_ORDER_CODE", request.getParamValue("SERVICE_ORDER_CODE"));
		paraMap.put("VIN", request.getParamValue("VIN"));
		paraMap.put("creatDate", request.getParamValue("creatDate"));
		paraMap.put("outPlantDate", request.getParamValue("outPlantDate"));
		paraMap.put("REPAIR_TYPE", request.getParamValue("REPAIR_TYPE"));
		paraMap.put("LICENSE_NO", request.getParamValue("LICENSE_NO"));
		paraMap.put("CTM_NAME", request.getParamValue("CTM_NAME"));
		String flag=request.getParamValue("flag");
		if("t".equals(flag)){
			//取得结果并返回
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; 
			logger.debug("=====================================aaa");	
			PageResult<Map<String,Object>> ps=dao.orderClaim(paraMap, curPage, pageSize);
			act.setOutData("ps", ps);
		}else{
			act.setForword(CLAIM_LIST_URL);
		}
		
		} catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * 索赔单新增跳转
	 */
	public void orderClaimAdd(){
		try{
		//传入参数
		Map<String,Object> paraMap=new HashMap<String,Object>();
		paraMap.put("so_id", request.getParamValue("so_id"));
		String repair_type=request.getParamValue("repair_type");//维修类型
		String activity_type=request.getParamValue("activity_type");//服务活动类型
		String so_id=request.getParamValue("so_id");
		String flag=request.getParamValue("flag");
		if("t".equals(flag)){
			//查询基础数据
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; 
			logger.debug("=====================================aaa");	
			PageResult<Map<String,Object>> ps=dao.orderClaim(paraMap, curPage, pageSize);
			act.setOutData("ListOrder", ps.getRecords().get(0));
			//维修类型是：首保，正常维修，外出维修展示工时配件
				//查询工时
				float hourNum = 0f;//工时总数
				float hourAmount = 0f;//工时总金额
				List<Map<String,Object>> listHours=dao.seOrderHours(Long.parseLong(so_id));
					if(listHours!=null && listHours.size()>0){
						for (int i = 0; i < listHours.size(); i++) {
							BigDecimal h1 = new BigDecimal(hourNum);   
							BigDecimal h2 = new BigDecimal(listHours.get(i).get("LABOUR_HOUR")+"");
							hourNum=h1.add(h2).floatValue();
							BigDecimal h3 = new BigDecimal(hourAmount);   
							BigDecimal h4 = new BigDecimal(listHours.get(i).get("LABOUR_AMOUNT")+"");
							hourAmount=h3.add(h4).floatValue();
						}
						act.setOutData("listHours", listHours);
					}
					if(hourNum>0){
						DecimalFormat df=new DecimalFormat("########.##");  
						String hdf1=df.format(hourNum);
						act.setOutData("hourNum", hdf1);
						if(Constant.REPAIR_TYPE_04.equals(repair_type) || (Constant.REPAIR_TYPE_05.equals(repair_type) && activity_type.equals(Constant.SERVICEACTIVITY_TYPE_NEW_02.toString()))){//首保的工时配件金额存0						
							hourAmount=0f;
							act.setOutData("hourAmount", "0");
						}else{
							String hdf2=df.format(hourAmount);
							act.setOutData("hourAmount", hdf2);
						}						
					}else{
						act.setOutData("hourNum", "0");
						act.setOutData("hourAmount", "0");
					}
					//查询外出项目
					float outAmount = 0f;//外出总金额
					List<Map<String,Object>> listOuts=dao.seOrderOuts(Long.parseLong(so_id));
						if(listOuts!=null && listOuts.size()>0){
							for (int i = 0; i < listOuts.size(); i++) {
								BigDecimal o1 = new BigDecimal(outAmount);   
								BigDecimal o2 = new BigDecimal(listOuts.get(i).get("FEE_PRICE")+"");
								outAmount=o1.add(o2).floatValue();								
							}
							act.setOutData("listOuts", listOuts);
						}
						if(outAmount>0){
							DecimalFormat df=new DecimalFormat("########.##");  
							String odf1=df.format(outAmount);
							act.setOutData("OUT_AMOUNT", odf1);												
						}else{
							act.setOutData("OUT_AMOUNT", "0");
						}					
				//查询配件
					float partNum = 0F;//配件总数
					float partAmount = 0F;//配件总金额
					List<Map<String,Object>> listParts=dao.seOrderParts(Long.parseLong(so_id));
					if(listParts!=null && listParts.size()>0){
						for (int j = 0; j < listParts.size(); j++) {
							BigDecimal p1 = new BigDecimal(partNum);   
							BigDecimal p2 = new BigDecimal(listParts.get(j).get("PART_NUM")+"");
							partNum=p1.add(p2).floatValue();
							BigDecimal p3 = new BigDecimal(partAmount);   
							BigDecimal p4 = new BigDecimal(listParts.get(j).get("PART_AMOUNT")+"");
							partAmount=p3.add(p4).floatValue();
						}
						act.setOutData("listParts", listParts);		
					}
					if(partNum>0){
						DecimalFormat df=new DecimalFormat("########.##");  
						String pdf1=df.format(partNum);
						act.setOutData("partNum", pdf1);
						if(Constant.REPAIR_TYPE_04.equals(repair_type) || (Constant.REPAIR_TYPE_05.equals(repair_type) && activity_type.equals(Constant.SERVICEACTIVITY_TYPE_NEW_02.toString()))){//首保的工时配件金额存0							
							partAmount=0f;
							act.setOutData("partAmount", "0");
						}else{
							String pdf2=df.format(partAmount);
							act.setOutData("partAmount", pdf2);
						}
					}else{
						act.setOutData("partNum", "0");
						act.setOutData("partAmount", "0");
					}					
				//总金额
					float hourPartAmount = 0F;//配件和工时总金额
					float Amount = 0F;//总金额
					BigDecimal z1 = new BigDecimal(hourAmount);   
					BigDecimal z2 = new BigDecimal(partAmount);
					hourPartAmount=z1.add(z2).floatValue();
					if(Constant.REPAIR_TYPE_01.equals(repair_type) || Constant.REPAIR_TYPE_06.equals(repair_type) || Constant.REPAIR_TYPE_09.equals(repair_type)){//一般维修，特殊服务，备件维修
						Amount=hourPartAmount;
					}else if(Constant.REPAIR_TYPE_02.equals(repair_type) || Constant.REPAIR_TYPE_03.equals(repair_type)){//外出维修或者售前维修
						BigDecimal t1 = new BigDecimal(hourPartAmount);   
						BigDecimal t2 = new BigDecimal(outAmount);//外出费用
						Amount=t1.add(t2).floatValue();
					}else if(Constant.REPAIR_TYPE_04.equals(repair_type)){//保养
						BigDecimal t1 = new BigDecimal(hourPartAmount);   
						BigDecimal t2 = new BigDecimal(ps.getRecords().get(0).get("APPLY_MAINTAIN_PRICE").toString());//保养费用
						Amount=t1.add(t2).floatValue();
					}else if(Constant.REPAIR_TYPE_05.equals(repair_type)){//服务活动
						if(activity_type.equals(Constant.SERVICEACTIVITY_TYPE_NEW_01.toString())){//技术升级
							BigDecimal t1 = new BigDecimal(hourPartAmount);   
							BigDecimal t2 = new BigDecimal(ps.getRecords().get(0).get("APPLY_ACTIVITY_PRICE_Z").toString());//外出费用
							Amount=t1.add(t2).floatValue();
						}else if(activity_type.equals(Constant.SERVICEACTIVITY_TYPE_NEW_02.toString())){//送保养
							BigDecimal t1 = new BigDecimal(hourPartAmount);   
							BigDecimal t2 = new BigDecimal(ps.getRecords().get(0).get("APPLY_ACTIVITY_PRICE_Z").toString());//外出费用
							Amount=t1.add(t2).floatValue();
						}else if(activity_type.equals(Constant.SERVICEACTIVITY_TYPE_NEW_03.toString())){//送检测
							Amount=Float.parseFloat(ps.getRecords().get(0).get("APPLY_ACTIVITY_PRICE_Z").toString());
						}
					}else if(Constant.REPAIR_TYPE_08.equals(repair_type)){//PDI
						BigDecimal t1 = new BigDecimal(hourPartAmount);   
						BigDecimal t2 = new BigDecimal(ps.getRecords().get(0).get("APPLY_PDI_PRICE").toString());//外出费用
						Amount=t1.add(t2).floatValue();						
					}
					DecimalFormat df=new DecimalFormat("########.##");  
					String Amount1=df.format(Amount);
					act.setOutData("Amount", Amount1);
					
				//配件加价率
					Map<String, Object> params1 = new HashMap<String, Object>();				
					params1.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
					params1.put("loginDealerId", CommonUtils.checkNull(ps.getRecords().get(0).get("DEALER_ID")));
					String partFareRate="";
					Map<String,Object> partFareRateMap = sdao.getPartFareRate(params1);
					if(partFareRate!=null&&!CommonUtils.checkNull(partFareRateMap.get("PART_FARE_RATE")).equals("")){
						partFareRate = CommonUtils.checkNull(partFareRateMap.get("PART_FARE_RATE"));
					}
					act.setOutData("partFareRate", partFareRate);
			}else{
				
			}				
				//附件
				getFile("so_id");					
			act.setForword(CLAIM_ADD_URL);		
		} catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * 索赔单新增验证（有索赔工时，就必须有索赔配件）
	 */
	public void claimVerification(){
		try{
			String[] labourId =request.getParamValues("labourId");//工时id
			String so_id =request.getParamValue("so_id");//工单id
			String message="";
			for(int i=0;i<labourId.length;i++){
				List<Map<String,Object>> listHours=dao.seAppClaimHoursId(Long.parseLong(labourId[i]),Long.parseLong(so_id));
				List<Map<String,Object>> listParts=dao.seOrderPartsSoId(Long.parseLong(labourId[i]),Long.parseLong(so_id));
				if(listParts.size()<1){
					message=message+listHours.get(0).get("LABOUR_CODE")+",";
					act.setOutData("msg", "00");
					act.setOutData("message", message);
				}
			}							
		} catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * 索赔单修改跳转
	 */
	public void appUpdate(){
		try{
		//传入参数
		Map<String,Object> paraMap=new HashMap<String,Object>();
		paraMap.put("id", request.getParamValue("sp_id"));
		String repair_type=request.getParamValue("repair_type");//维修类型
		String sp_id=request.getParamValue("sp_id");
		String flag=request.getParamValue("flag");
		if("t".equals(flag)){
				//查询基础数据
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; 
				logger.debug("=====================================aaa");	
				PageResult<Map<String,Object>> ps=dao.applicationClaim(paraMap, curPage, pageSize);
				act.setOutData("ListOrder", ps.getRecords().get(0));
				//外出费用
				float outAmount = 0f;
				if(ps.getRecords().size()>0){
					outAmount=Float.parseFloat(ps.getRecords().get(0).get("OUT_AMOUNT").toString());
					if(outAmount>0){
						DecimalFormat odf=new DecimalFormat("########.##");  
						String out_Amount=odf.format(outAmount);
						act.setOutData("OUT_AMOUNT", out_Amount);
					}else{
						act.setOutData("OUT_AMOUNT", '0');
					}				
				}													
				//查询工时
				float hourNum = 0f;//工时总数
				float hourAmount = 0f;//工时总金额
				List<Map<String,Object>> listHours=dao.seAppClaimHours(Long.parseLong(sp_id));
					if(listHours!=null && listHours.size()>0){
						for (int i = 0; i < listHours.size(); i++) {
							BigDecimal h1 = new BigDecimal(hourNum);   
							BigDecimal h2 = new BigDecimal(listHours.get(i).get("LABOUR_HOUR")+"");
							hourNum=h1.add(h2).floatValue();
							BigDecimal h3 = new BigDecimal(hourAmount);   
							BigDecimal h4 = new BigDecimal(listHours.get(i).get("LABOUR_AMOUNT")+"");
							hourAmount=h3.add(h4).floatValue();
						}
						act.setOutData("listHours", listHours);
					}			
						DecimalFormat df=new DecimalFormat("########.##");  
						String h1=df.format(hourNum);
						String h2=df.format(hourAmount);
						act.setOutData("hourNum", h1);						
						act.setOutData("hourAmount", h2);
						//查询外出项目
						List<Map<String,Object>> listOuts=dao.seClaimOuts(Long.parseLong(sp_id));
							if(listOuts!=null && listOuts.size()>0){
								act.setOutData("listOuts", listOuts);
							}												
				//查询配件
					float partNum = 0F;//配件总数
					float partAmount = 0F;//配件总金额
					List<Map<String,Object>> listParts=dao.seAppClaimParts(Long.parseLong(sp_id));
					if(listParts!=null && listParts.size()>0){
						for (int j = 0; j < listParts.size(); j++) {
							BigDecimal p1 = new BigDecimal(partNum);   
							BigDecimal p2 = new BigDecimal(listParts.get(j).get("PART_NUM")+"");
							partNum=p1.add(p2).floatValue();
							BigDecimal p3 = new BigDecimal(partAmount);   
							BigDecimal p4 = new BigDecimal(listParts.get(j).get("PART_APPLY_AMOUNT")+"");
							partAmount=p3.add(p4).floatValue();
						}
						act.setOutData("listParts", listParts);		
					}
						String p1=df.format(partNum);
						String p2=df.format(partAmount);
						String p3=df.format(Float.parseFloat(ps.getRecords().get(0).get("APPLY_TOTAL_AMOUNT").toString()));
						act.setOutData("partNum", p1);						
						act.setOutData("partAmount", p2);											
						act.setOutData("Amount", p3);
					//加价率
					Map<String, Object> params1 = new HashMap<String, Object>();				
					params1.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
					params1.put("loginDealerId", CommonUtils.checkNull(ps.getRecords().get(0).get("DEALER_ID")));
					String partFareRate="";
					Map<String,Object> partFareRateMap = sdao.getPartFareRate(params1);
					if(partFareRate!=null&&!CommonUtils.checkNull(partFareRateMap.get("PART_FARE_RATE")).equals("")){
						partFareRate = CommonUtils.checkNull(partFareRateMap.get("PART_FARE_RATE"));
					}
					act.setOutData("partFareRate", partFareRate);
			}						
			act.setForword(CLAIM_ADD_URL);		
		} catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * 查询顾客问题大系统
	 */
	public void cusProblem(){
		try{
		//传入参数
			//查询大系统
			List<Map<String,String>> list1=dao.cusProblemList1();
			if(list1!=null && list1.size()>0){
				act.setOutData("listCus1", list1);
			}		
			act.setForword(CUS_PROBLEM_URL);	
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询顾客问题");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	/**
	 * 查询顾客问题子系统
	 */
	public void cusProblemz(){
		try{
		//传入参数			
			//查询子系统
			String VRT_CODE=request.getParamValue("VRT_CODE");
			List<Map<String,String>> list2=dao.cusProblemList2(VRT_CODE);
			if(list2!=null && list2.size()>0){
				act.setOutData("listCus2", list2);
			}				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询顾客问题子系统");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	/**
	 * 查询顾客问题
	 */
	public void seCusProblem(){
		try{
		//传入参数
		Map<String,String> paraMap=new HashMap<String,String>();
		paraMap.put("VRT_CODE", request.getParamValue("VRT_CODE"));
		paraMap.put("VFG_CODE", request.getParamValue("VFG_CODE"));
		paraMap.put("CCC_NAME", request.getParamValue("CCC_NAME"));
		String flag=request.getParamValue("flag");
		if("t".equals(flag)){
			//取得结果并返回
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; 
			logger.debug("=====================================aaa");	
			PageResult<Map<String,String>> ps=dao.cusProblem(paraMap, curPage, pageSize);
			act.setOutData("ps", ps);
		}		
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件弹窗跳转");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	/**
	 * @description 选择旧件弹窗跳转
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void servicePartWin(){
		try {
			String arrivalDate = CommonUtils.checkNull(request.getParamValue("arrivalDate"));//进站时间
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//进站里程数
			String repair = CommonUtils.checkNull(request.getParamValue("repair"));
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));//购车时间
			String partFareRate = CommonUtils.checkNull(request.getParamValue("partFareRate"));//经销商加价率
			String idOldPartId = CommonUtils.checkNull(request.getParamValue("idOldPartId"));//父页面旧件id
			String idOldPartCode = CommonUtils.checkNull(request.getParamValue("idOldPartCode"));//父页面旧件code
			String idOldPartName = CommonUtils.checkNull(request.getParamValue("idOldPartName"));//父页面旧件name
			act.setOutData("arrivalDate", arrivalDate);
			act.setOutData("mileage", mileage);
			act.setOutData("repair", repair);
			act.setOutData("purchasedDate", purchasedDate);
			act.setOutData("partFareRate", partFareRate);
			act.setOutData("idOldPartId", idOldPartId);
			act.setOutData("idOldPartCode", idOldPartCode);
			act.setOutData("idOldPartName", idOldPartName);
			
			act.setForword(APP_CLAIM_PART_WIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件弹窗跳转");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	/**
	 * @description 旧件配件查询
	 * @Date 2017-08-04
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void servicePartQuery(){
		try {
			//页面参数
//			String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));//车型ID
			String arrivalDate = CommonUtils.checkNull(request.getParamValue("arrivalDate"));//进站时间
			String mileage = CommonUtils.checkNull(request.getParamValue("mileage"));//进站里程数
			String purchasedDate = CommonUtils.checkNull(request.getParamValue("purchasedDate"));//购车时间
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));//配件代码
			String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));//配件名称
			String repair = CommonUtils.checkNull(request.getParamValue("repair"));//工单类型
			String partFareRate = CommonUtils.checkNull(request.getParamValue("partFareRate"));//经销商加价率
			
			Map<String,Object>params = new HashMap<String, Object>();
			
//			params.put("modelId", modelId);
			params.put("arrivalDate", arrivalDate);
			params.put("mileage", mileage);
			params.put("purchasedDate", purchasedDate);
			params.put("partCode", partCode);
			params.put("partCname", partCname);
			params.put("partFareRate", partFareRate);
			params.put("repairType", repair);
			params.put("loginUserId", CommonUtils.checkNull(logonUser.getUserId()));
			params.put("loginDealerId", CommonUtils.checkNull(logonUser.getDealerId()));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>>ps = sdao.servicePartQuery(params,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}		
	}
	private void getFile(String ywzjName) {
		String ywzj = DaoFactory.getParam(request, ywzjName);
		List<FsFileuploadPO> fileList = claimservice.queryAttById(ywzj);// 取得附件
		act.setOutData("fileList", fileList);
	}
	//索赔单保存
	@SuppressWarnings("unchecked")
	public void appClaimUpdate() {		
		try {
			//索赔基础数据			
			String so_id = CommonUtils.checkNull(request.getParamValue("so_id"));//工单id
			String sp_id = CommonUtils.checkNull(request.getParamValue("sp_id"));//索赔单id
			String applyLabourPrice = CommonUtils.checkNull(request.getParamValue("applyLabourPrice"));//工时总金额
			String applyPartPrice = CommonUtils.checkNull(request.getParamValue("applyPartPrice"));//配件总金额
			String applyPriceTotal = CommonUtils.checkNull(request.getParamValue("applyPriceTotal"));//总金额
			//查询工单
			TtAsServiceOrderPO order=new TtAsServiceOrderPO();
				order.setServiceOrderId(Long.parseLong(so_id));
			TtAsServiceOrderPO seorder=(TtAsServiceOrderPO) dao.select(order).get(0);
			//索赔单主表信息
			TtAsWrApplicationClaimPO appClaim=new TtAsWrApplicationClaimPO();
			TtAsWrApplicationClaimPO upAppClaim=new TtAsWrApplicationClaimPO();
			appClaim.setDealerId(seorder.getDealerId());//经销商id
			appClaim.setRepairType(seorder.getRepairType());//维修类型
			appClaim.setArrivalDate(seorder.getArrivalDate());//进站日期
			appClaim.setRepairDateBegin(seorder.getRepairDateBegin());//维修开始时间
			appClaim.setRepairDateEnd(seorder.getRepairDateEnd());//维修结束时间
			appClaim.setMileage(seorder.getMileage());//进站日期
			appClaim.setReceptionistMan(seorder.getReceptionistMan());//接待人
			appClaim.setVin(seorder.getVin());//vin
			appClaim.setDelivererManName(seorder.getDelivererManName());//送修人姓名
			appClaim.setDelivererManPhone(seorder.getDelivererManPhone());//送修人电话
			appClaim.setFaultDesc(seorder.getFaultDesc());//故障描述
			appClaim.setFaultReason(seorder.getFaultReason());//申请内容，故障原因
			appClaim.setRepairMethod(seorder.getRepairMethod());//申请内容维修措施
			appClaim.setApplyRemark(seorder.getRemark());//申请备注
			appClaim.setServiceOrderCode(seorder.getServiceOrderCode());//工单号
			appClaim.setServiceOrderId(Long.parseLong(so_id));//工单id
			appClaim.setPartApplyAmount(Float.parseFloat(applyPartPrice));//配件申请金额
			appClaim.setHoursApplyAmount(Float.parseFloat(applyLabourPrice));////工时申请金额
			appClaim.setApplyTotalAmount(Float.parseFloat(applyPriceTotal));//申请总金额
			appClaim.setPartSettlementAmount(Float.parseFloat(applyPartPrice));//配件结算金额
			appClaim.setHoursSettlementAmount(Float.parseFloat(applyLabourPrice));////工时结算金额
			appClaim.setSettlementTotalAmount(Float.parseFloat(applyPriceTotal));//结算总金额
			appClaim.setPartClaimAmount(Float.parseFloat(applyPartPrice));//配件索赔金额
			appClaim.setHoursClaimAmount(Float.parseFloat(applyLabourPrice));////工时索赔金额
			appClaim.setClaimTotalAmount(Float.parseFloat(applyPriceTotal));//索赔总金额
			//外出维修
			String out_amount = CommonUtils.checkNull(request.getParamValue("out_amount"));//外出金额
			appClaim.setEgressId(seorder.getEgressId());//外出维修ID
			appClaim.setOutwardApplyAmount(Float.parseFloat(out_amount));//外出维修申请费用
			appClaim.setOutwardSettlementAmount(Float.parseFloat(out_amount));//外出维修结算费用
			appClaim.setOutwardClaimAmount(Float.parseFloat(out_amount));//外出维修索赔费用
			//pdi
			appClaim.setPdiApplyAmount(seorder.getApplyPdiPrice());//pdi申请金额
			appClaim.setPdiSettlementAmount(seorder.getApplyPdiPrice());//pdi结算金额
			appClaim.setPdiClaimAmount(seorder.getApplyPdiPrice());//pdi索赔金额
			appClaim.setPdiResult(seorder.getPdiRemark());//pdi结果
			//保养
			appClaim.setMaintenanceTime(seorder.getCurFreeTimes());//保养次数
			appClaim.setFirstApplyAmount(seorder.getApplyMaintainPrice());//保养申请金额
			appClaim.setFirstSettlementAmount(seorder.getApplyMaintainPrice());//保养结算金额
			appClaim.setFirstClaimAmount(seorder.getApplyMaintainPrice());//保养索赔金额
			//服务活动
			String activity_price = CommonUtils.checkNull(request.getParamValue("activity_price"));//服务活动折扣后金额
			appClaim.setActivityId(seorder.getActivityId());//服务活动id
			appClaim.setActivitieApplyAmount(Float.parseFloat(activity_price));//服务活动折扣后申请金额
			appClaim.setActivitieSettlementAmount(Float.parseFloat(activity_price));//服务活动折扣后结算金额
			appClaim.setActivitieClaimAmount(Float.parseFloat(activity_price));//服务活动折扣后索赔金额
			if("".equals(sp_id)){//新增
				appClaim.setId(Long.parseLong(SequenceManager.getSequence("")));
				DateFormat format = new SimpleDateFormat("yyMMdd"); 
				String Numberdate = format.format(new Date());
				String serialNumber=GenerateNumber("TT_AS_WR_APPLICATION_CLAIM", "APP_CLAIM_NO", "CREATE_DATE");
				appClaim.setAppClaimNo("SSSP"+Numberdate+serialNumber);//单据编码
				appClaim.setStatus(Constant.APP_CLAIM_TYPE_01);//索赔单状态-未上报
				appClaim.setCreateDate(new Date());
				appClaim.setCreateBy(loginUser.getUserId());				
				dao.insert(appClaim);
				
				//索赔工时子表添加
				String[] labourId =request.getParamValues("labourId");//工时id
				String[] labourCode =request.getParamValues("labourCode");//工时代码
				String[] cnDes =request.getParamValues("cnDes");//工时名称
				String[] labourHour =request.getParamValues("labourHour");//工时定额
				String[] labourPrice =request.getParamValues("labourPrice");//工时单价
				String[] labourPriceTotal =request.getParamValues("labourPriceTotal");//工时金额(元)
				String[] cusProblem =request.getParamValues("cusProblem");//顾客问题
				if(labourId!=null && labourId.length>0){
					for(int i=0;i<labourId.length;i++){
						TtAsWrAppProjectPO pjp =new TtAsWrAppProjectPO();
						pjp.setClaimProjectId(Long.parseLong(SequenceManager.getSequence("")));
						pjp.setAppClaimId(appClaim.getId());
						pjp.setLabourId(Long.parseLong(labourId[i]));
						pjp.setLabourCode(labourCode[i]);
						pjp.setCnDes(cnDes[i]);
						pjp.setLabourHour(Float.parseFloat(labourHour[i]));
						pjp.setLabourSettlementHour(Float.parseFloat(labourHour[i]));
						pjp.setLabourClaimHour(Float.parseFloat(labourHour[i]));
						pjp.setLabourPrice(Double.parseDouble(labourPrice[i]));
						pjp.setHoursApplyAmount(Float.parseFloat(labourPriceTotal[i]));
						pjp.setHoursSettlementAmount(Float.parseFloat(labourPriceTotal[i]));
						pjp.setHoursClaimAmount(Float.parseFloat(labourPriceTotal[i]));
						if(!"".equals(cusProblem) && cusProblem!=null){
							pjp.setCustomerProblem(cusProblem[i]);
						}
						
						pjp.setCreateDate(new Date());
						pjp.setCreateBy(loginUser.getUserId());
						dao.insert(pjp);
					}
				}
				
				//索赔外出子表添加
				String[] fee_id =request.getParamValues("fee_id");//项目id
				String[] fee_code =request.getParamValues("fee_code");//项目代码
				String[] fee_name =request.getParamValues("fee_name");//项目名称
				String[] fee_price =request.getParamValues("fee_price");//金额
				String[] fee_remark =request.getParamValues("fee_remark");//备注
				String[] fee_relation_main_part =request.getParamValues("fee_relation_main_part");//主因件
				if(fee_id!=null && fee_id.length>0){
					for(int i=0;i<fee_id.length;i++){
						TtAsWrAppOutPO pop =new TtAsWrAppOutPO();
						pop.setOutId(Long.parseLong(SequenceManager.getSequence("")));
						pop.setAppclaimId(appClaim.getId());
						pop.setFeeId(Long.parseLong(fee_id[i]));
						pop.setFeeCode(fee_code[i]);
						pop.setFeeName(fee_name[i]);
						pop.setFeePrice(Double.parseDouble(fee_price[i]));
						pop.setFeeSettlementPrice(Double.parseDouble(fee_price[i]));
						pop.setFeeClaimPrice(Double.parseDouble(fee_price[i]));
						pop.setFeeRemark(fee_remark[i]);
						pop.setFeeRelationMainPart(Long.parseLong(fee_relation_main_part[i]));
						
						pop.setCreateDate(new Date());
						pop.setCreateBy(loginUser.getUserId());
						dao.insert(pop);
					}
				}
				
				//索赔配件子表添加
				String[] isThreeGuarantee =request.getParamValues("isThreeGuarantee");//是否三包
				String[] partOldId =request.getParamValues("partOldId");//旧件id
				String[] partOldCode =request.getParamValues("partOldCode");//旧件代码
				String[] partOldName =request.getParamValues("partOldName");//旧件名称
				String[] partId =request.getParamValues("partId");//新件id
				String[] partCode =request.getParamValues("partCode");//新件代码
				String[] partCname =request.getParamValues("partCname");//新件名称
				String[] partNum =request.getParamValues("partNum");//配件数量
				String[] partPrice =request.getParamValues("partPrice");//单价
				String[] partPriceTotal =request.getParamValues("partPriceTotal");//金额
				String[] OldPartGCode =request.getParamValues("OldPartGCode");//旧件供应商代码
				String[] OldPartGName =request.getParamValues("OldPartGName");//旧件供应商名称
				String[] IS_MAIN_PART =request.getParamValues("IS_MAIN_PART");//是否主因件
				String[] RELATION_MAIN_PART =request.getParamValues("RELATION_MAIN_PART");//关联主因件
				String[] RELATION_LABOUR =request.getParamValues("RELATION_LABOUR");//关联工时
				String[] PART_USE_TYPE =request.getParamValues("PART_USE_TYPE");//配件使用类型
				if(partOldId!=null && partOldId.length>0){
					for(int j=0;j<partOldId.length;j++){
						TtAsWrAppPartPO part =new TtAsWrAppPartPO();
						part.setClaimPartId(Long.parseLong(SequenceManager.getSequence("")));
						part.setAppClaimId(appClaim.getId());
						part.setIsThreeGuarantee(Integer.parseInt(isThreeGuarantee[j])); 
						part.setPartId(Long.parseLong(partId[j]));
						part.setPartCode(partCode[j]);
						part.setPartCname(partCname[j]);
						part.setPartNum(Integer.parseInt(partNum[j]));
						part.setPartSettlementNum(Integer.parseInt(partNum[j]));
						part.setPartClaimNum(Integer.parseInt(partNum[j]));
						part.setSalePrice(Double.parseDouble(partPrice[j]));
						part.setIsMainPart(Integer.parseInt(IS_MAIN_PART[j]));
						part.setPartApplyAmount(Float.parseFloat(partPriceTotal[j]));
						part.setPartSettlementAmount(Float.parseFloat(partPriceTotal[j]));
						part.setPartClaimAmount(Float.parseFloat(partPriceTotal[j]));
						part.setOldPartId(Long.parseLong(partOldId[j]));
						part.setOldPartCode(partOldCode[j]);
						part.setOldPartCname(partOldName[j]);
						part.setReponseSupplierCode(OldPartGCode[j]);
						part.setReponseSupplierName(OldPartGName[j]);
						part.setClaimSupplierCode(OldPartGCode[j]);
						part.setClaimSupplierName(OldPartGName[j]);
						part.setCreateDate(new Date());
						part.setCreateBy(loginUser.getUserId());
						if(!"".equals(RELATION_MAIN_PART[j]) && RELATION_MAIN_PART[j]!=null){
							part.setRelationMainPart(Long.parseLong(RELATION_MAIN_PART[j]));
						}
						if(!"".equals(RELATION_LABOUR[j]) && RELATION_LABOUR[j]!=null){
							part.setRelationLabour(Long.parseLong(RELATION_LABOUR[j]));
						}
						if(!"".equals(PART_USE_TYPE[j]) && PART_USE_TYPE[j]!=null){
							part.setPartUseType(Integer.parseInt(PART_USE_TYPE[j]));
						}
						//判断旧件是否需要回运
						TmPtPartBasePO pb=new TmPtPartBasePO();
						pb.setPartCode(partOldCode[j]);
						TmPtPartBasePO sepb=(TmPtPartBasePO) dao.select(pb).get(0);
						part.setIsRecycle(sepb.getIsReturn());
						dao.insert(part);
					}
				}
				
				//修改工单是否已生成索赔单
				TtAsServiceOrderPO serOrder=new TtAsServiceOrderPO();
				serOrder.setServiceOrderId(Long.parseLong(so_id));
				TtAsServiceOrderPO upSerOrder=new TtAsServiceOrderPO();
				upSerOrder.setIsBuildClaim(10041001);
				dao.update(serOrder, upSerOrder);
			}else{//修改
				upAppClaim.setId(Long.parseLong(sp_id));
				appClaim.setUpdateDate(new Date());
				appClaim.setUpdateBy(loginUser.getUserId());
				dao.update(upAppClaim,appClaim);
				//删除索赔子表信息
				TtAsWrAppProjectPO dpjp =new TtAsWrAppProjectPO();
				dpjp.setAppClaimId(Long.parseLong(sp_id));
				dao.delete(dpjp);
				TtAsWrAppOutPO dpop =new TtAsWrAppOutPO();
				dpop.setAppclaimId(Long.parseLong(sp_id));
				dao.delete(dpop);
				TtAsWrAppPartPO dpart =new TtAsWrAppPartPO();
				dpart.setAppClaimId(Long.parseLong(sp_id));
				dao.delete(dpart);
				//索赔工时子表添加
				String[] labourId =request.getParamValues("labourId");//工时id
				String[] labourCode =request.getParamValues("labourCode");//工时代码
				String[] cnDes =request.getParamValues("cnDes");//工时名称
				String[] labourHour =request.getParamValues("labourHour");//工时定额
				String[] labourPrice =request.getParamValues("labourPrice");//工时单价
				String[] labourPriceTotal =request.getParamValues("labourPriceTotal");//工时金额(元)
				String[] cusProblem =request.getParamValues("cusProblem");//顾客问题
				if(labourId!=null && labourId.length>0){
					for(int i=0;i<labourId.length;i++){
						TtAsWrAppProjectPO pjp =new TtAsWrAppProjectPO();
						pjp.setClaimProjectId(Long.parseLong(SequenceManager.getSequence("")));
						pjp.setAppClaimId(Long.parseLong(sp_id));
						pjp.setLabourId(Long.parseLong(labourId[i]));
						pjp.setLabourCode(labourCode[i]);
						pjp.setCnDes(cnDes[i]);
						pjp.setLabourHour(Float.parseFloat(labourHour[i]));
						pjp.setLabourSettlementHour(Float.parseFloat(labourHour[i]));
						pjp.setLabourClaimHour(Float.parseFloat(labourHour[i]));
						pjp.setLabourPrice(Double.parseDouble(labourPrice[i]));
						pjp.setHoursApplyAmount(Float.parseFloat(labourPriceTotal[i]));
						pjp.setHoursSettlementAmount(Float.parseFloat(labourPriceTotal[i]));
						pjp.setHoursClaimAmount(Float.parseFloat(labourPriceTotal[i]));
						if(!"".equals(cusProblem) && cusProblem!=null){
							pjp.setCustomerProblem(cusProblem[i]);
						}
						pjp.setCreateDate(new Date());
						pjp.setCreateBy(loginUser.getUserId());
						dao.insert(pjp);
					}
				}
				//索赔外出子表添加
				String[] fee_id =request.getParamValues("fee_id");//项目id
				String[] fee_code =request.getParamValues("fee_code");//项目代码
				String[] fee_name =request.getParamValues("fee_name");//项目名称
				String[] fee_price =request.getParamValues("fee_price");//金额
				String[] fee_remark =request.getParamValues("fee_remark");//备注
				String[] fee_relation_main_part =request.getParamValues("fee_relation_main_part");//主因件
				if(fee_id!=null && fee_id.length>0){
					for(int i=0;i<fee_id.length;i++){
						TtAsWrAppOutPO pop =new TtAsWrAppOutPO();
						pop.setOutId(Long.parseLong(SequenceManager.getSequence("")));
						pop.setAppclaimId(appClaim.getId());
						pop.setFeeId(Long.parseLong(fee_id[i]));
						pop.setFeeCode(fee_code[i]);
						pop.setFeeName(fee_name[i]);
						pop.setFeePrice(Double.parseDouble(fee_price[i]));
						pop.setFeeSettlementPrice(Double.parseDouble(fee_price[i]));
						pop.setFeeClaimPrice(Double.parseDouble(fee_price[i]));
						pop.setFeeRemark(fee_remark[i]);
						pop.setFeeRelationMainPart(Long.parseLong(fee_relation_main_part[i]));
						
						pop.setCreateDate(new Date());
						pop.setCreateBy(loginUser.getUserId());
						dao.insert(pop);
					}
				}
				//索赔配件子表添加
				String[] isThreeGuarantee =request.getParamValues("isThreeGuarantee");//是否三包
				String[] partOldId =request.getParamValues("partOldId");//旧件id
				String[] partOldCode =request.getParamValues("partOldCode");//旧件代码
				String[] partOldName =request.getParamValues("partOldName");//旧件名称
				String[] partId =request.getParamValues("partId");//新件id
				String[] partCode =request.getParamValues("partCode");//新件代码
				String[] partCname =request.getParamValues("partCname");//新件名称
				String[] partNum =request.getParamValues("partNum");//配件数量
				String[] partPrice =request.getParamValues("partPrice");//单价
				String[] partPriceTotal =request.getParamValues("partPriceTotal");//金额
				String[] OldPartGCode =request.getParamValues("OldPartGCode");//旧件供应商代码
				String[] OldPartGName =request.getParamValues("OldPartGName");//旧件供应商名称
				String[] IS_MAIN_PART =request.getParamValues("IS_MAIN_PART");//是否主因件
				String[] RELATION_MAIN_PART =request.getParamValues("RELATION_MAIN_PART");//关联主因件
				String[] RELATION_LABOUR =request.getParamValues("RELATION_LABOUR");//关联工时
				String[] PART_USE_TYPE =request.getParamValues("PART_USE_TYPE");//配件使用类型
				if(partCode!=null && partCode.length>0){
					for(int j=0;j<partCode.length;j++){
						TtAsWrAppPartPO part =new TtAsWrAppPartPO();
						part.setClaimPartId(Long.parseLong(SequenceManager.getSequence("")));
						part.setAppClaimId(Long.parseLong(sp_id));
						part.setIsThreeGuarantee(Integer.parseInt(isThreeGuarantee[j])); 
						part.setPartId(Long.parseLong(partId[j]));
						part.setPartCode(partCode[j]);
						part.setPartCname(partCname[j]);
						part.setPartNum(Integer.parseInt(partNum[j]));
						part.setPartSettlementNum(Integer.parseInt(partNum[j]));
						part.setPartClaimNum(Integer.parseInt(partNum[j]));
						part.setSalePrice(Double.parseDouble(partPrice[j]));
						part.setIsMainPart(Integer.parseInt(IS_MAIN_PART[j]));
						part.setPartApplyAmount(Float.parseFloat(partPriceTotal[j]));
						part.setPartSettlementAmount(Float.parseFloat(partPriceTotal[j]));
						part.setPartClaimAmount(Float.parseFloat(partPriceTotal[j]));
						part.setOldPartId(Long.parseLong(partOldId[j]));
						part.setOldPartCode(partOldCode[j]);
						part.setOldPartCname(partOldName[j]);
						part.setReponseSupplierCode(OldPartGCode[j]);
						part.setReponseSupplierName(OldPartGName[j]);
						part.setClaimSupplierCode(OldPartGCode[j]);
						part.setClaimSupplierName(OldPartGName[j]);
						part.setCreateDate(new Date());
						part.setCreateBy(loginUser.getUserId());
						if(!"".equals(RELATION_MAIN_PART[j]) && RELATION_MAIN_PART[j]!=null){
							part.setRelationMainPart(Long.parseLong(RELATION_MAIN_PART[j]));
						}
						if(!"".equals(RELATION_LABOUR[j]) && RELATION_LABOUR[j]!=null){
							part.setRelationLabour(Long.parseLong(RELATION_LABOUR[j]));
						}
						if(!"".equals(PART_USE_TYPE[j]) && PART_USE_TYPE[j]!=null){
							part.setPartUseType(Integer.parseInt(PART_USE_TYPE[j]));
						}
						//判断旧件是否需要回运
						TmPtPartBasePO pb=new TmPtPartBasePO();
						pb.setPartCode(partOldCode[j]);
						TmPtPartBasePO sepb=(TmPtPartBasePO) dao.select(pb).get(0);
						part.setIsRecycle(sepb.getIsReturn());
						dao.insert(part);
				}
				}
		}
			act.setOutData("msg", "0");
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "维修配件查询");
			logger.error(logonUser, e1);
			act.setActionReturn(e1);
		}
	}
	//生成序列号-经销商id字段名，id值，表名，要生成的编码字段名，创建时间字段名
			public String GenerateNumber(String po,String obtainName,String createDateName){
				String no="";
				try {
					List<Map<String, Object>> list = dao.generateNumber(po,obtainName,createDateName);
					if(list.size()>0){
						no=list.get(0).get(obtainName).toString();
						no=no.substring(no.length()-4, no.length());
						Long max=Long.parseLong(no)+1;
						no=max.toString();
						int a=4-no.length();
						for(int i=0;i<a;i++){
							no="0"+no;
						}

					}else{
						no="0001";
					}
				}	catch (Exception e) {
					BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
					e.printStackTrace();
					act.setException(e1);
				}
				return no;
			}
			/**
			 * 查询索赔单
			 */
			public void applicationClaim(){
				try{
				//传入参数
				Map<String,Object> paraMap=new HashMap<String,Object>();
				if(!"".equals(logonUser.getDealerId()) && logonUser.getDealerId()!=null){
					paraMap.put("dealerdId", logonUser.getDealerId());
				}else{
					paraMap.put("dealerdId", request.getParamValue("SERVICE_ORDER_CODE"));
				}				
				paraMap.put("SERVICE_ORDER_CODE", request.getParamValue("SERVICE_ORDER_CODE"));
				paraMap.put("VIN", request.getParamValue("VIN"));
				paraMap.put("creatDate", request.getParamValue("creatDate"));
				paraMap.put("outPlantDate", request.getParamValue("outPlantDate"));
				//paraMap.put("REPAIR_TYPE", request.getParamValue("REPAIR_TYPE"));
				paraMap.put("LICENSE_NO", request.getParamValue("LICENSE_NO"));
				paraMap.put("APP_CLAIM_NO", request.getParamValue("APP_CLAIM_NO"));
				String flag=request.getParamValue("flag");
				if("t".equals(flag)){
					//取得结果并返回
					
					Integer curPage = request.getParamValue("curPage") != null ? Integer
							.parseInt(request.getParamValue("curPage"))
							: 1; 
					logger.debug("=====================================aaa");	
					PageResult<Map<String,Object>> ps=dao.applicationClaim(paraMap, curPage, pageSize);
					act.setOutData("ps", ps);
				}else{
					act.setForword(APPCLAIM_LIST_URL);
				}
				
				} catch (Exception e) {
					BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
					e.printStackTrace();
					act.setException(e1);
				}
			}
			/**
			 * 索赔单上报
			 */
			@SuppressWarnings("unchecked")
			public void appReport(){
				try{
				//传入参数				
				String id=request.getParamValue("id");
				if(!"".equals(id)){
					TtAsWrApplicationClaimPO appClaim=new TtAsWrApplicationClaimPO();
					appClaim.setId(Long.parseLong(id));
					TtAsWrApplicationClaimPO upAppClaim=new TtAsWrApplicationClaimPO();
					TtAsWrApplicationClaimPO seAppClaim=(TtAsWrApplicationClaimPO) dao.select(appClaim).get(0);
					if(!Constant.APP_CLAIM_TYPE_01.equals(seAppClaim.getStatus()) && !Constant.APP_CLAIM_TYPE_03.equals(seAppClaim.getStatus())){
						act.setOutData("msg", "01");
					}else{
						upAppClaim.setStatus(Constant.APP_CLAIM_TYPE_02);
						dao.update(appClaim, upAppClaim);
						act.setOutData("msg", "00");
					}
				}
				
				} catch (Exception e) {
					BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
					e.printStackTrace();
					act.setException(e1);
				}
			}
			/**
			 * 查询索赔单一级审核
			 */
			public void applicationClaimOne(){
				try{
				//传入参数
				Map<String,Object> paraMap=new HashMap<String,Object>();				
				paraMap.put("dealerdId", request.getParamValue("dealerId"));
				paraMap.put("factory1", "factory1");			
				paraMap.put("SERVICE_ORDER_CODE", request.getParamValue("SERVICE_ORDER_CODE"));
				paraMap.put("VIN", request.getParamValue("VIN"));
				paraMap.put("creatDate", request.getParamValue("creatDate"));
				paraMap.put("outPlantDate", request.getParamValue("outPlantDate"));
				//paraMap.put("REPAIR_TYPE", request.getParamValue("REPAIR_TYPE"));
				paraMap.put("LICENSE_NO", request.getParamValue("LICENSE_NO"));
				paraMap.put("APP_CLAIM_NO", request.getParamValue("APP_CLAIM_NO"));
				String flag=request.getParamValue("flag");
				if("t".equals(flag)){
					//取得结果并返回
					
					Integer curPage = request.getParamValue("curPage") != null ? Integer
							.parseInt(request.getParamValue("curPage"))
							: 1; 
					logger.debug("=====================================aaa");	
					PageResult<Map<String,Object>> ps=dao.applicationClaim(paraMap, curPage, pageSize);
					act.setOutData("ps", ps);
				}else{
					act.setForword(APPCLAIM_FACTORY_ONE_LIST_URL);
					act.setOutData("audit", "one");
				}
				
				} catch (Exception e) {
					BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
					e.printStackTrace();
					act.setException(e1);
				}
			}
			/**
			 * 查询索赔单二级审核
			 */
			public void applicationClaimTwo(){
				try{
				//传入参数
				Map<String,Object> paraMap=new HashMap<String,Object>();

				paraMap.put("dealerdId", request.getParamValue("dealerId"));
				paraMap.put("factory2", "factory2");								
				paraMap.put("SERVICE_ORDER_CODE", request.getParamValue("SERVICE_ORDER_CODE"));
				paraMap.put("VIN", request.getParamValue("VIN"));
				paraMap.put("creatDate", request.getParamValue("creatDate"));
				paraMap.put("outPlantDate", request.getParamValue("outPlantDate"));
				//paraMap.put("REPAIR_TYPE", request.getParamValue("REPAIR_TYPE"));
				paraMap.put("LICENSE_NO", request.getParamValue("LICENSE_NO"));
				paraMap.put("APP_CLAIM_NO", request.getParamValue("APP_CLAIM_NO"));
				String flag=request.getParamValue("flag");
				if("t".equals(flag)){
					//取得结果并返回
					
					Integer curPage = request.getParamValue("curPage") != null ? Integer
							.parseInt(request.getParamValue("curPage"))
							: 1; 
					logger.debug("=====================================aaa");	
					PageResult<Map<String,Object>> ps=dao.applicationClaim(paraMap, curPage, pageSize);
					act.setOutData("ps", ps);
				}else{
					act.setForword(APPCLAIM_FACTORY_TWO_LIST_URL);
					act.setOutData("audit", "two");					
				}
				
				} catch (Exception e) {
					BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
					e.printStackTrace();
					act.setException(e1);
				}
			}
			/**
			 * 查询索赔单查询
			 */
			public void applicationClaimSe(){
				try{
				//传入参数
				Map<String,Object> paraMap=new HashMap<String,Object>();

				paraMap.put("dealerdId", request.getParamValue("dealerId"));
				paraMap.put("factory3", "factory3");								
				paraMap.put("SERVICE_ORDER_CODE", request.getParamValue("SERVICE_ORDER_CODE"));
				paraMap.put("VIN", request.getParamValue("VIN"));
				paraMap.put("creatDate", request.getParamValue("creatDate"));
				paraMap.put("outPlantDate", request.getParamValue("outPlantDate"));
				//paraMap.put("REPAIR_TYPE", request.getParamValue("REPAIR_TYPE"));
				paraMap.put("LICENSE_NO", request.getParamValue("LICENSE_NO"));
				paraMap.put("APP_CLAIM_NO", request.getParamValue("APP_CLAIM_NO"));
				String flag=request.getParamValue("flag");
				if("t".equals(flag)){
					//取得结果并返回
					
					Integer curPage = request.getParamValue("curPage") != null ? Integer
							.parseInt(request.getParamValue("curPage"))
							: 1; 
					logger.debug("=====================================aaa");	
					PageResult<Map<String,Object>> ps=dao.applicationClaim(paraMap, curPage, pageSize);
					act.setOutData("ps", ps);
				}else{
					act.setForword(APPCLAIM_FACTORY_SE_LIST_URL);				
				}
				
				} catch (Exception e) {
					BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
					e.printStackTrace();
					act.setException(e1);
				}
			}
			/**
			 * 索赔单审核
			 */
			@SuppressWarnings("unchecked")
			public void auditApp(){
				try{
				String idArray=request.getParamValue("idArray");//id集合
				String id=request.getParamValue("id");//id
				String s=request.getParamValue("s");//审核类型
				String audit=request.getParamValue("audit");//几级审核
				String flag=request.getParamValue("flag");
				if("t".equals(flag)){
					String[] strId = idArray.split(",");
					for(int i=0;i<strId.length;i++ ){
						String idss = strId[i];
						String auditRemark=request.getParamValue("auditRemark");//审核意见
						if("one".equals(audit)){//一级审核
							if("1".equals(s)){//通过
								TtAsWrApplicationClaimPO po = new TtAsWrApplicationClaimPO();
								TtAsWrApplicationClaimPO uppo = new TtAsWrApplicationClaimPO();
								po.setId(Long.parseLong(idss));				
								//审核					
								uppo.setAuthAuditBy(loginUser.getUserId());
								uppo.setAuthAuditDate(new Date());
								uppo.setStatus(Constant.APP_CLAIM_TYPE_04);				
								dao.update(po, uppo);
								//审核日志
								insertAuditRecord(Constant.APP_CLAIM_TYPE_04.toString(), logonUser.getUserId(), idss,auditRemark);
							}else if("2".equals(s)){//驳回
								TtAsWrApplicationClaimPO po = new TtAsWrApplicationClaimPO();
								TtAsWrApplicationClaimPO uppo = new TtAsWrApplicationClaimPO();
								po.setId(Long.parseLong(idss));				
								//审核					
								uppo.setAuthAuditBy(loginUser.getUserId());
								uppo.setAuthAuditDate(new Date());
								uppo.setStatus(Constant.APP_CLAIM_TYPE_03);			
								dao.update(po, uppo);
								//审核日志
								insertAuditRecord(Constant.APP_CLAIM_TYPE_03.toString(), logonUser.getUserId(), idss,auditRemark);
							}
							else if("3".equals(s)){//拒绝
								TtAsWrApplicationClaimPO po = new TtAsWrApplicationClaimPO();
								TtAsWrApplicationClaimPO uppo = new TtAsWrApplicationClaimPO();
								po.setId(Long.parseLong(idss));				
								//审核					
								uppo.setAuthAuditBy(loginUser.getUserId());
								uppo.setAuthAuditDate(new Date());
								uppo.setStatus(Constant.APP_CLAIM_TYPE_06);			
								dao.update(po, uppo);
								//审核日志
								insertAuditRecord(Constant.APP_CLAIM_TYPE_06.toString(), logonUser.getUserId(), idss,auditRemark);
							}
						}else{//二级审核
							if("1".equals(s)){//通过
								TtAsWrApplicationClaimPO po = new TtAsWrApplicationClaimPO();
								TtAsWrApplicationClaimPO uppo = new TtAsWrApplicationClaimPO();
								po.setId(Long.parseLong(idss));				
								//审核					
								uppo.setAuthAuditBy(loginUser.getUserId());
								uppo.setAuthAuditDate(new Date());
								uppo.setStatus(Constant.APP_CLAIM_TYPE_05);					
								dao.update(po, uppo);
								//审核日志
								insertAuditRecord(Constant.APP_CLAIM_TYPE_05.toString(), logonUser.getUserId(), idss,auditRemark);
							}else if("2".equals(s)){//驳回
								TtAsWrApplicationClaimPO po = new TtAsWrApplicationClaimPO();
								TtAsWrApplicationClaimPO uppo = new TtAsWrApplicationClaimPO();
								po.setId(Long.parseLong(idss));				
								//审核					
								uppo.setAuthAuditBy(loginUser.getUserId());
								uppo.setAuthAuditDate(new Date());
								uppo.setStatus(Constant.APP_CLAIM_TYPE_03);			
								dao.update(po, uppo);
								//审核日志
								insertAuditRecord(Constant.APP_CLAIM_TYPE_03.toString(), logonUser.getUserId(), idss,auditRemark);
							}
							else if("3".equals(s)){//拒绝
								TtAsWrApplicationClaimPO po = new TtAsWrApplicationClaimPO();
								TtAsWrApplicationClaimPO uppo = new TtAsWrApplicationClaimPO();
								po.setId(Long.parseLong(idss));				
								//审核					
								uppo.setAuthAuditBy(loginUser.getUserId());
								uppo.setAuthAuditDate(new Date());
								uppo.setStatus(Constant.APP_CLAIM_TYPE_06);				
								dao.update(po, uppo);
								//审核日志
								insertAuditRecord(Constant.APP_CLAIM_TYPE_06.toString(), logonUser.getUserId(), idss,auditRemark);
							}
						}	
					}					
					act.setOutData("msg", "0");
				}else{
					//查询基础数据
					Map<String,Object> paraMap=new HashMap<String,Object>();
					paraMap.put("id", id);
					Integer curPage = request.getParamValue("curPage") != null ? Integer
							.parseInt(request.getParamValue("curPage"))
							: 1; 
					logger.debug("=====================================aaa");	
					PageResult<Map<String,Object>> ps=dao.applicationClaim(paraMap, curPage, pageSize);
					act.setOutData("ListOrder", ps.getRecords().get(0));
					//查询工时
					float hourNum = 0f;//工时总数
					List<Map<String,Object>> listHours=dao.seAppClaimHours(Long.parseLong(id));
						if(listHours!=null && listHours.size()>0){
							for (int i = 0; i < listHours.size(); i++) {
								BigDecimal h1 = new BigDecimal(hourNum);   
								BigDecimal h2 = new BigDecimal(listHours.get(i).get("LABOUR_HOUR")+"");
								hourNum=h1.add(h2).floatValue();								
							}
							act.setOutData("listHours", listHours);							
						}
						if(hourNum>0){
							DecimalFormat df=new DecimalFormat("########.##");  
							String h1=df.format(hourNum);
							act.setOutData("hourNum", h1);
						}else{
							act.setOutData("hourNum", "0");
						}
						//查询外出项目
						List<Map<String,Object>> listOuts=dao.seClaimOuts(Long.parseLong(id));
							if(listOuts!=null && listOuts.size()>0){								
								act.setOutData("listOuts", listOuts);
							}	
					//查询配件
						float partNum = 0F;//配件总数
						List<Map<String,Object>> listParts=dao.seAppClaimParts(Long.parseLong(id));
						if(listParts!=null && listParts.size()>0){
							for (int j = 0; j < listParts.size(); j++) {
								BigDecimal p1 = new BigDecimal(partNum);   
								BigDecimal p2 = new BigDecimal(listParts.get(j).get("PART_NUM")+"");
								partNum=p1.add(p2).floatValue();
							}
							act.setOutData("listParts", listParts);									
						}	
						if(partNum>0){
							DecimalFormat df=new DecimalFormat("########.##");  
							String p1=df.format(partNum);
							act.setOutData("partNum", p1);
						}else{
							act.setOutData("partNum", "0");
						}
					act.setOutData("audit", audit);
					List<Map<String, Object>> rList=dao.specialRecord(id);
					act.setOutData("rList", rList);
					if("se".equals(flag)){
						act.setForword(APPCLAIM_FIND_URL);
					}else if("au".equals(flag)){
						act.setForword(APPCLAIM_AUDIT_URL);
					}
				}
				
				} catch (Exception e) {
					BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
					e.printStackTrace();
					act.setException(e1);
				}
			}
			/**
			 * 插入审核日志
			 * @param status
			 * @param userId
			 * @param speId
			 */
			public void insertAuditRecord(String status, Long userId, String speId,String c) {
				try {
					TtAsWrAuditRecordPO po=new TtAsWrAuditRecordPO();
					po.setAuditBy(userId);
					po.setAuditDate(new Date());
					po.setOperaStstus(Integer.parseInt(status));
					po.setId(DaoFactory.getPkId());
					po.setAppClaimId(Long.parseLong(speId));
					po.setAuditRecord(c);
					dao.insert(po);
				} catch (Exception e) {
					BizException e1 = new BizException(act,ErrorCodeConstant.FAILURE_CODE, e.getMessage());
					e.printStackTrace();
					act.setException(e1);
				}
			}
}
