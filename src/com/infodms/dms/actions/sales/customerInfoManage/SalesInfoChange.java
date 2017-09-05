package com.infodms.dms.actions.sales.customerInfoManage;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.FileUpLoadDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtCustomerEditLogPO;
import com.infodms.dms.po.TtDealerActualSalesAuditPO;
import com.infodms.dms.po.TtLinkmanPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;

/**   
 * @Title  : SalesInfoChange.java
 * @Package: com.infodms.dms.actions.sales.customerInfoManage
 * @Description: 实销信息更改申请
 * @date   : 2010-6-28 
 * @version: V1.0   
 */
public class SalesInfoChange  extends BaseDao{
	public Logger logger = Logger.getLogger(SalesInfoChange.class);
	private ActionContext act = ActionContext.getContext();
	private static final SalesInfoChange dao = new SalesInfoChange();
	public static final SalesInfoChange getInstance() {
		return dao;
	}
	RequestWrapper request = act.getRequest();
	private final String  salesInfoChangeInit = "/jsp/sales/customerInfoManage/salesInfoChangeInit.jsp";
	
	/** 
	* @Title	  : salesInfoChangeInit 
	* @Description: 实销信息更改申请页面初始化 
	* @throws 
	* @LastUpdate :2010-6-28
	*/
	public void salesInfoChangeInit(){
		AclUserBean logonUser = null;
		try {
			
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList1 = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			
			Integer oemFlag = CommonUtils.getNowSys(Long.parseLong(logonUser.getOemCompanyId())) ;
			
			act.setOutData("oemFlag", oemFlag) ;
			act.setOutData("areaList", areaList1);
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal=Calendar.getInstance();
			//cal.add(Calendar.MONTH, -3);    //得到前一个月
			
			long date = cal.getTimeInMillis();
			//act.setOutData("startDate", df.format(new Date(date)));
			act.setOutData("startDate", "2013-01-01");
			act.setForword(salesInfoChangeInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改申请页面初始化 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : salesInfoChangeAction 
	* @Description: 实销信息更改申请：提报
	* @throws 
	* @LastUpdate :2010-6-29
	*/
	@SuppressWarnings("unchecked")
	public void salesInfoChangeAction(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String vehicle_id = CommonUtils.checkNull(request.getParamValue("vehicle_id"));						//要更改的车辆id 
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));							//实销id 
			String ctmId = CommonUtils.checkNull(request.getParamValue("ctmId"));								//客户id 
			String salesDate = CommonUtils.checkNull(request.getParamValue("salesDate"));						//实销时间
			String isFleet = CommonUtils.checkNull(request.getParamValue("isFleet"));							//是否是集团客户
			String edit_remark = CommonUtils.checkNull(request.getParamValue("edit_remark"));					//修改原因
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet"));
			String secondeVeh = CommonUtils.checkNull(request.getParamValue("secondeVeh"));						//是否二手车置换
			//根据vehicle_id 修改车辆的状态为锁定
			TmVehiclePO tp=new TmVehiclePO();
			tp.setVehicleId(Long.parseLong(vehicle_id));
			TmVehiclePO tpnew=new TmVehiclePO();
			tpnew.setLockStatus(Constant.LOCK_STATUS_09);
			dao.update(tp,tpnew);
			/*
			 * 1.销售信息
			 * */
			String vehicle_no = CommonUtils.checkNull(request.getParamValue("vehicle_no"));						//车牌号
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no"));					//合同编号
			String invoice_date = CommonUtils.checkNull(request.getParamValue("invoice_date"));					//开票日期
			String invoice_no = CommonUtils.checkNull(request.getParamValue("invoice_no"));						//发票编号
			String insurance_company = CommonUtils.checkNull(request.getParamValue("insurance_company"));		//保险公司
			String insurance_date = CommonUtils.checkNull(request.getParamValue("insurance_date"));				//保险日期
			String vchlPro = CommonUtils.checkNull(request.getParamValue("carCharactor"));							//车辆性质
			String consignation_date = CommonUtils.checkNull(request.getParamValue("consignation_date"));		//车辆交付日期
			String miles = CommonUtils.checkNull(request.getParamValue("miles"));								//交付时公里数
			String payment = CommonUtils.checkNull(request.getParamValue("payment"));							//付款方式
			String price = CommonUtils.checkNull(request.getParamValue("price"));								//价格
			String memo = CommonUtils.checkNull(request.getParamValue("memo"));									//备注
			
			/*
			 * 2.客户信息
			 * */	
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type"));			    //选择客户类型
			String myctm_form = CommonUtils.checkNull(request.getParamValue("myctm_form"));			    //选择公司客户来源
			String company_name = CommonUtils.checkNull(request.getParamValue("company_name"));			    	//公司名称
			String company_s_name = CommonUtils.checkNull(request.getParamValue("company_s_name"));				//公司简称
			String level_id = CommonUtils.checkNull(request.getParamValue("level_id"));							//公司规模
			String kind = CommonUtils.checkNull(request.getParamValue("kind"));									//公司性质
			String company_phone = CommonUtils.checkNull(request.getParamValue("company_phone"));				//公司电话
			String vehicle_num = CommonUtils.checkNull(request.getParamValue("vehicle_num"));					//目前车辆数
			String company_code = CommonUtils.checkNull(request.getParamValue("company_code"));// 公司组织代码
			String company_man = CommonUtils.checkNull(request.getParamValue("company_man"));// 公司联系人
			String company_tel = CommonUtils.checkNull(request.getParamValue("company_tel"));// 公司联系电话
			String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));							//客户姓名
			String sex = CommonUtils.checkNull(request.getParamValue("sex"));									//性别
			String card_type = CommonUtils.checkNull(request.getParamValue("card_type"));						//证件类别
			String card_num = CommonUtils.checkNull(request.getParamValue("card_num"));							//证件号码
			String main_phone = CommonUtils.checkNull(request.getParamValue("main_phone"));						//主要联系电话
			String other_phone = CommonUtils.checkNull(request.getParamValue("other_phone"));					//其他联系电话
			String email = CommonUtils.checkNull(request.getParamValue("email"));								//电子邮件
			String post_code = CommonUtils.checkNull(request.getParamValue("post_code"));						//邮编
			String birthday = CommonUtils.checkNull(request.getParamValue("birthday"));							//出生年月
			String ctm_form = CommonUtils.checkNull(request.getParamValue("ctm_form"));							//客户来源
			String income = CommonUtils.checkNull(request.getParamValue("income"));								//家庭月收入
			String education = CommonUtils.checkNull(request.getParamValue("education"));						//教育程度
			String industry = CommonUtils.checkNull(request.getParamValue("industry"));							//所在行业
			String is_married = CommonUtils.checkNull(request.getParamValue("is_married"));						//婚姻状况
			String profession = CommonUtils.checkNull(request.getParamValue("profession"));						//职业
			String job = CommonUtils.checkNull(request.getParamValue("job"));									//职务
			String province = CommonUtils.checkNull(request.getParamValue("province"));							//省份
			String city = CommonUtils.checkNull(request.getParamValue("city"));									//地级市
			String district = CommonUtils.checkNull(request.getParamValue("district"));							// 区、县
			String address = CommonUtils.checkNull(request.getParamValue("address"));							//详细地址
			String city1 = CommonUtils.checkNull(request.getParamValue("city1"));									//地级市
			String district1 = CommonUtils.checkNull(request.getParamValue("district1"));							// 区、县
			String province1 = CommonUtils.checkNull(request.getParamValue("province1"));							//省份
			String knowaddress = CommonUtils.checkNull(request.getParamValue("knowaddress"));									//地级市
			String salesreson = CommonUtils.checkNull(request.getParamValue("salesreson"));							// 区、县
			//String salesAddress = CommonUtils.checkNull(request.getParamValue("salesAddress"));	//详细地址
			String salesAddress=null; 

			if (customer_type !=null && !"".equals(customer_type) && customer_type.equals(Constant.CUSTOMER_TYPE_02 + "")) {
				salesAddress = CommonUtils.checkNull(request.getParamValue("salesAddress")); // 公司购买用途
			}else{
				salesAddress = CommonUtils.checkNull(request.getParamValue("salesAddress1")); // 个人购买用途
			}
		
			String mortgageType = CommonUtils.checkNull(request.getParamValue("mortgageType"));					//	按揭类型
			String FirstPrice = CommonUtils.checkNull(request.getParamValue("FirstPrice"));						//		首付比例
			String LoansType = CommonUtils.checkNull(request.getParamValue("LoansType"));						//贷款方式
			String LoansYear = CommonUtils.checkNull(request.getParamValue("LoansYear"));						//	贷款年限
			String money=CommonUtils.checkNull(request.getParamValue("money"));									//贷款金额
			String bank=CommonUtils.checkNull(request.getParamValue("bank"));									//贷款银行
			String lv=CommonUtils.checkNull(request.getParamValue("lv"));										//贷款利率
			
			String thischange=CommonUtils.checkNull(request.getParamValue("thischange"));	
			String loanschange=CommonUtils.checkNull(request.getParamValue("loanschange"));	
			
			//String fjId = CommonUtils.checkNull(request.getParamValue("fjid"));//获取上传修改文件的ID号
			//System.out.println("fjId:"+fjId);
			String[] fids = request.getParamValues("fjid");//获取文件ID
			String tflag = "";
			if(fids!=null){//2017-7-26
				FileUpLoadDAO fdaos = new FileUpLoadDAO();
				List<DynaBean> fLists=fdaos.getIfUpdateFile(fids);
				DynaBean dbs = fLists.get(0);
				tflag = dbs.get("TFLAG").toString();
			}

			
			
			TtDealerActualSalesAuditPO actualSalesAuditPO = new TtDealerActualSalesAuditPO();
			actualSalesAuditPO.setKnowAddress(knowaddress);
			actualSalesAuditPO.setSalesReson(salesreson);
			actualSalesAuditPO.setSalesAddress(salesAddress);
			Long log_Id=Long.parseLong(SequenceManager.getSequence(""));
			actualSalesAuditPO.setLogId(log_Id);
			actualSalesAuditPO.setVehicleId(Long.parseLong(vehicle_id));
			actualSalesAuditPO.setOrderId(Long.parseLong(orderId));
			//建立修改附件，附件信息和实销更改申请ID之间的关联关系
			if (!tflag.equals("2")) {
				String[] fjids = request.getParamValues("fjid");//获取文件ID
				String fId=null;
				String fileYwzj=null;
				if(fjids!=null&&fjids.length>0){
					for(int i=0;i<fjids.length;i++){
						if(fjids[i]!=null||!"".equals(fjids[i])){
							fId=fjids[i];
							
						}
						FileUpLoadDAO fdao = new FileUpLoadDAO();
						List<DynaBean> fList=fdao.relationCheckFile(fId);
						DynaBean db = fList.get(0);
						String flag = db.get("FLAG").toString();
						
						//通过审核更改申请ID查询是否有修改上传附件信息
						FsFileuploadPO xPO = new FsFileuploadPO();
						xPO.setFjid(Long.parseLong(fId));
						List xlist=dao.select(xPO);
						if(flag.equals("1")){//业务号不为空表示此文件没有做更改审核的时候，要复制一份文件进行审核
							String fileUrl=((FsFileuploadPO) xlist.get(i)).getFileurl();
							String fileName=((FsFileuploadPO) xlist.get(i)).getFilename();
							String fileId=((FsFileuploadPO) xlist.get(i)).getFileid();
							
							FsFileuploadPO Insertpo = new FsFileuploadPO();
							Insertpo.setFileid(fileId);
							Insertpo.setFileurl(fileUrl);
							Insertpo.setFilename(fileName);
							Insertpo.setYwzj(log_Id);
							FileUpLoadDAO dao = new FileUpLoadDAO();
							dao.addDisableFile(Insertpo, logonUser);
						}else if(flag.equals("0")){
							fdao.updateRelationFile(log_Id.toString(),fId,logonUser);
							
						}
						
						}
					}
			}
			
			if (null != vehicle_no && !"".equals(vehicle_no)) {
				actualSalesAuditPO.setVehicleNo(vehicle_no.trim());
			}
			if (null != contract_no && !"".equals(contract_no)) {
				actualSalesAuditPO.setContractNo(contract_no.trim());
			}
			actualSalesAuditPO.setInvoiceDate(dateFormat.parse(invoice_date));
			actualSalesAuditPO.setInvoiceNo(invoice_no.trim());
			actualSalesAuditPO.setCarCharactor(new Long(vchlPro));
			if (null != insurance_company && !"".equals(insurance_company)) {
				actualSalesAuditPO.setInsuranceCompany(insurance_company.trim());
			}
			if (null != insurance_date && !"".equals(insurance_date)) {
				actualSalesAuditPO.setInsuranceDate(dateFormat.parse(insurance_date));
			}
			/*actualSalesAuditPO.setConsignationDate(dateFormat.parse(consignation_date));
			if (null != miles && !"".equals(miles) && !"0.0".equals(miles)) {
				actualSalesAuditPO.setMiles(Float.parseFloat(miles.trim()));
			}*/
			actualSalesAuditPO.setPayment(Integer.parseInt(payment));
			actualSalesAuditPO.setPrice(Double.parseDouble(price.trim()));
			
			if (null != mortgageType && !"".equals(mortgageType)) {
				actualSalesAuditPO.setMortgageType(Long.valueOf(mortgageType));
			}
			if (null != FirstPrice && !"".equals(FirstPrice)) {
				actualSalesAuditPO.setShoufuRatio(Double.valueOf(FirstPrice)/100);
			}
			if (null != LoansType && !"".equals(LoansType)) {
				actualSalesAuditPO.setLoansType(Long.valueOf(LoansType));
			}
			if (null != LoansYear && !"".equals(LoansYear)) {
				actualSalesAuditPO.setLoansYear(Long.valueOf(LoansYear));
			}
			
			if (null != memo && !"".equals(memo)) {
				actualSalesAuditPO.setMemo(memo.trim());
			}
			if (null != money && !"".equals(money)) {
				actualSalesAuditPO.setMoney(Double.valueOf(money.trim()));
			}
			if (null != bank && !"".equals(bank)) {
				actualSalesAuditPO.setBank(Long.valueOf(bank.trim()));
			}
			if (null != lv && !"".equals(lv)) {
				actualSalesAuditPO.setLv(Double.valueOf(lv.trim()));
			}
			
			if (null != thischange && !"".equals(thischange)) {
				actualSalesAuditPO.setThischange(Long.valueOf(thischange.trim()));
			}
			if (null != loanschange && !"".equals(loanschange)) {
				actualSalesAuditPO.setLoanschange(loanschange.trim());
			}
			//2.如果是集团客户
			
			
			if(is_fleet==""){
				/*if (null != isFleet && isFleet.equals(Constant.IF_TYPE_YES+"") ) {
					String fleet_id = CommonUtils.checkNull(request.getParamValue("fleet_id"));						   //集团客户id
					String fleet_contract_no_id = CommonUtils.checkNull(request.getParamValue("fleet_contract_no_id"));//集团客户合同id
					actualSalesAuditPO.setIsFleet(Constant.IF_TYPE_YES);
					actualSalesAuditPO.setFleetId(Long.parseLong(fleet_id));
					actualSalesAuditPO.setContractId(Long.parseLong(fleet_contract_no_id));
				}*/
			}else if(Integer.parseInt(is_fleet)==Constant.IF_TYPE_NO){
				actualSalesAuditPO.setIsFleet(Constant.IF_TYPE_NO);
			}else{
				String fleet_id = CommonUtils.checkNull(request.getParamValue("fleet_id"));						   //集团客户id
				String fleet_contract_no_id = CommonUtils.checkNull(request.getParamValue("fleet_contract_no_id"));//集团客户合同id
				actualSalesAuditPO.setIsFleet(Constant.IF_TYPE_YES);
				actualSalesAuditPO.setFleetId(Long.parseLong(fleet_id));
				if(fleet_contract_no_id!=""){
					actualSalesAuditPO.setContractId(Long.parseLong(fleet_contract_no_id));
				}else{
					actualSalesAuditPO.setContractId(Long.parseLong("0"));
				}
			}
			
			actualSalesAuditPO.setCtmId(Long.parseLong(ctmId));
			
			
			TtCustomerEditLogPO customerEditLogPO = new TtCustomerEditLogPO();
			Long ctmEditId = Long.parseLong(SequenceManager.getSequence(""));
			customerEditLogPO.setCtmEditId(ctmEditId);
			customerEditLogPO.setCtmId(Long.parseLong(ctmId));
			customerEditLogPO.setCtmType(Integer.parseInt(customer_type));
			customerEditLogPO.setIsReturn("1");
			//如果客户类型是“公司客户”
			if (null != customer_type && !"".equals(customer_type) && (Constant.CUSTOMER_TYPE_02+"").equals(customer_type)) {
				if (null != company_name && !"".equals(company_name)) {
					customerEditLogPO.setCompanyName(company_name);
				}
				if(null!=myctm_form && !"".equals(myctm_form)){
					customerEditLogPO.setCtmForm(Integer.parseInt(myctm_form));//公司客户来源
					
				}
				if (null != company_s_name && !"".equals(company_s_name)) {
					customerEditLogPO.setCompanySName(company_s_name.trim());
				}
				if (null != company_phone && !"".equals(company_phone)) {
					customerEditLogPO.setCompanyPhone(company_phone.trim());
				}
				customerEditLogPO.setLevelId(Integer.parseInt(level_id));
				customerEditLogPO.setKind(Integer.parseInt(kind));
				if (null != vehicle_num && !"".equals(vehicle_num)) {
					customerEditLogPO.setVehicleNum(Integer.parseInt(vehicle_num.trim()));
				}
				if (null != province && !"".equals(province)) {
					customerEditLogPO.setProvince(Long.parseLong(province));
				}
				if (null != city && !"".equals(city)) {
					customerEditLogPO.setCity(Long.parseLong(city));
				}
				if (null != district && !"".equals(district)) {
					customerEditLogPO.setTown(Long.parseLong(district));
				}
				customerEditLogPO.setCompanyCode(company_code);
				customerEditLogPO.setCtmName(company_man);
				customerEditLogPO.setMainPhone(company_tel);
			}else{
				customerEditLogPO.setCtmName(ctmName);
				customerEditLogPO.setSex(Integer.parseInt(sex));
				customerEditLogPO.setCardType(Integer.parseInt(card_type));
				customerEditLogPO.setCardNum(card_num.trim());
				customerEditLogPO.setMainPhone(main_phone.trim());
				
				if (null != other_phone && !"".equals(other_phone)) {
					customerEditLogPO.setOtherPhone(other_phone.trim());
				}
				if (null != email && !"".equals(email)) {
					customerEditLogPO.setEmail(email.trim());
				}
				if (null != post_code && !"".equals(post_code)) {
					customerEditLogPO.setPostCode(post_code.trim());
				}
				customerEditLogPO.setBirthday(dateFormat.parse(birthday));
				customerEditLogPO.setCtmForm(Integer.parseInt(ctm_form));
				
				customerEditLogPO.setIncome(Integer.parseInt(income));
				customerEditLogPO.setEducation(Integer.parseInt(education));
				customerEditLogPO.setIndustry(Integer.parseInt(industry));
				customerEditLogPO.setIsMarried(Integer.parseInt(is_married));
				customerEditLogPO.setProfession(Integer.parseInt(profession));
				if (null != job && !"".equals(job)) {
					customerEditLogPO.setJob(job.trim());
				}
				if (null != province1 && !"".equals(province1)) {
					customerEditLogPO.setProvince(Long.parseLong(province1));
				}
				if (null != city1 && !"".equals(city1)) {
					customerEditLogPO.setCity(Long.parseLong(city1));
				}
				if (null != district1 && !"".equals(district1)) {
					customerEditLogPO.setTown(Long.parseLong(district1));
				}
				customerEditLogPO.setAddress(address.trim());
			}
			
			customerEditLogPO.setEditRemark(edit_remark.trim());
			customerEditLogPO.setSubmitUser(logonUser.getUserId());
			customerEditLogPO.setCreateBy(logonUser.getUserId());
			customerEditLogPO.setCreateDate(new Date());
			//客户信息更改
			dao.insert(customerEditLogPO);
			
			actualSalesAuditPO.setCtmEditId(ctmEditId);
			actualSalesAuditPO.setStatus(Constant.SALES_INFO_CHANGE_STATUS_01);
			actualSalesAuditPO.setSalesDate(dateFormat.parse(salesDate));
			actualSalesAuditPO.setCreateDate(new Date());
			actualSalesAuditPO.setCreateBy(logonUser.getUserId());
			actualSalesAuditPO.setAuditDate(new Date());
			
			if(null != is_fleet && is_fleet.equals(Constant.IF_TYPE_YES+"") ) {
				actualSalesAuditPO.setFlagFleet(Constant.FLAG_FLEET_01.longValue());
			}else{
				actualSalesAuditPO.setFlagFleet(Constant.FLAG_FLEET_02.longValue());
			}
			
			//TODO 新增是否为二手车置换将置换标识插入临时表中 2012-07-03 韩晓宇	
			if(secondeVeh != null && !"".equals(secondeVeh)) {
				actualSalesAuditPO.setIsSecond(Integer.valueOf(secondeVeh.toString()));		
			}
			//TODO END
			
			//实销信息更改
			dao.insert(actualSalesAuditPO);
			
			//zhumingwei 2011-09-21    把实销大客户状态变为未审核
			/*if(null != is_fleet && is_fleet.equals(Constant.IF_TYPE_YES+"") ) {
				TtDealerActualSalesPO po =new TtDealerActualSalesPO();
				po.setOrderId(Long.parseLong(orderId));
				TtDealerActualSalesPO poValue =new TtDealerActualSalesPO();
				
				String fleet_id = CommonUtils.checkNull(request.getParamValue("fleet_id"));						   //集团客户id
				String fleet_contract_no_id = CommonUtils.checkNull(request.getParamValue("fleet_contract_no_id"));//集团客户合同id
				String fleet_contract_no = CommonUtils.checkNull(request.getParamValue("fleet_contract_no"));
				poValue.setIsFleet(Constant.IF_TYPE_YES);			
				poValue.setFleetId(Long.parseLong(fleet_id));			
				poValue.setContractId(Long.parseLong(fleet_contract_no_id));		
				poValue.setContractNo(fleet_contract_no);		//大客户合同号
				poValue.setFleetStatus(Constant.Fleet_SALES_CHECK_STATUS_01);							//未审核
				
				poValue.setUpdateBy(logonUser.getUserId());
				poValue.setUpdateDate(new Date());
				
				dao.update(po, poValue);
			}*/
			//zhumingwei 2011-09-21
			
			/*
			 * 查询：
			 * 1.车厂公司ID：	OEM_COMPANY_ID
			 * 2.经销商公司ID：	DLR_COMPANY_ID
			 * 3.经销商渠道：	DEALER_ID
			 * */
			String s_dealer_id = "";		//经销商渠道
			String s_oem_company_id = "";	//车厂公司ID
			String s_dlr_company_id = "";	//经销商公司ID
			
			TmVehiclePO tmVehiclePO = new TmVehiclePO();
			tmVehiclePO.setVehicleId(Long.parseLong(vehicle_id));
			List vehicleList = dao.select(tmVehiclePO);
			if (null != vehicleList && vehicleList.size()>0) {
				s_dealer_id = ((TmVehiclePO)vehicleList.get(0)).getDealerId()+"";
				TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setDealerId(((TmVehiclePO)vehicleList.get(0)).getDealerId());
				List dealerList = dao.select(dealerPO);
				if (null != dealerList && dealerList.size()>0) {
					s_oem_company_id = ((TmDealerPO)dealerList.get(0)).getOemCompanyId()+"";
					s_dlr_company_id = ((TmDealerPO)dealerList.get(0)).getCompanyId()+""; 
				}
			}
			String[] linkMan_name = request.getParamValues("linkMan_name"); 
			String[] linkMan_main_phone = request.getParamValues("linkMan_main_phone"); 
			String[] linkMan_other_phone = request.getParamValues("linkMan_other_phone"); 
			String[] linkMan_contract_reason = request.getParamValues("linkMan_contract_reason"); 
			if (null != linkMan_name && linkMan_name.length>0) {
				for (int i = 0; i < linkMan_name.length; i++) {
					TtLinkmanPO linkmanPO = new TtLinkmanPO();
					linkmanPO.setLmId(Long.parseLong(SequenceManager.getSequence("")));			//联系人ID
					linkmanPO.setCtmId(Long.parseLong(ctmId));					            	//客户序列号	
					
					if (null != s_dlr_company_id && !"".equals(s_dlr_company_id)) {
						linkmanPO.setDlrCompanyId(Long.parseLong(s_dlr_company_id));			//经销商公司
					}
					if (null != s_oem_company_id && !"".equals(s_oem_company_id)) {
						linkmanPO.setOemCompanyId(Long.parseLong(s_oem_company_id));			//车厂公司ID
					}
					linkmanPO.setName(linkMan_name[i].trim());									//姓名
					linkmanPO.setMainPhone(linkMan_main_phone[i].trim());						//主要联系电话
					if (null != linkMan_other_phone[i] && !"".equals(linkMan_other_phone[i])) {
						linkmanPO.setOtherPhone(linkMan_other_phone[i].trim());					//其他联系电话
					}
					if (null != linkMan_contract_reason[i] && !"".equals(linkMan_contract_reason[i])) {
						linkmanPO.setContractReason(linkMan_contract_reason[i].trim());			//联系目的
					}
					linkmanPO.setCreateBy(logonUser.getUserId());
					linkmanPO.setCreateDate(new Date());
					dao.insert(linkmanPO);
				}
			}
			salesInfoChangeInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改申请：提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
