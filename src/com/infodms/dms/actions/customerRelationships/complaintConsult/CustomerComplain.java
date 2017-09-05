package com.infodms.dms.actions.customerRelationships.complaintConsult;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.dao.customerRelationships.CustomerComplainDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCustomerComplainPO;
import com.infodms.dms.po.TtCustomerComplainRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 待处理投诉查询ACTIONS
 * @ClassName     : CustomerComplain 
 * @Description   : 待处理投诉查询
 * @author        : wangMing
 * CreateDate     : 2014-11-28
 */
public class CustomerComplain {
	private static Logger logger = Logger.getLogger(CustomerComplain.class);
	//抱怨新增页面
	private final String CUSTOMER_COMPLAIN_ADD = "/jsp/customerRelationships/complaintConsult/customerComplainAdd.jsp";
	private final String CUSTOMER_COMPLAIN_INIT = "/jsp/customerRelationships/complaintConsult/customerComplainInit.jsp";
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	CustomerComplainDao dao = CustomerComplainDao.getInstance();
	
	String SGENJIN_DATE;
	String EGENJIN_DATE;
	String SDENGJI_DATE;
	String EDENGJI_DATE;
	String COMPLAINT_LEVEL;
	String COMPLAINT_TYPE;
	String guzhang_miaosu;
	String VIN;
	String status;
	String dealerCode;
	String cmd;
	String isDealer;
	//客户抱怨登记
	public void customerComplainInit(){
		try{	
			if(logonUser.getDealerId()!=null){
				act.setOutData("isDealer", true);
			}else{
				act.setOutData("isDealer", false);
			}
			act.setForword(CUSTOMER_COMPLAIN_INIT);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户抱怨登记初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
		//客户抱怨查询
		public void customerComplainQueryInit(){
			try{	
				if(logonUser.getDealerId()!=null){
					act.setOutData("isDealer", true);
				}else{
					act.setOutData("isDealer", false);
				}
				act.setForword(CUSTOMER_COMPLAIN_INIT);
			} catch(Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户抱怨登记初始化");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
	public void customerComplainAdd(){
		try{	
			act.setOutData("now", CommonUtils.getDate());
			act.setOutData("isAdd", true);
			act.setForword(CUSTOMER_COMPLAIN_ADD);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户抱怨登记新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void customerComplainFollow(){
		try{	
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			TtCustomerComplainPO complainPO = new TtCustomerComplainPO();
			complainPO.setId(new Long(id));
			CustomerComplainDao dao = CustomerComplainDao.getInstance();
			List <TtCustomerComplainPO> complainPOS = dao.select(complainPO);
			act.setOutData("complainPOS", complainPOS.get(0));
			
			TtCustomerComplainRecordPO complainRecordPO = new TtCustomerComplainRecordPO();
			complainRecordPO.setDetailId(new Long(id));
			List <TtCustomerComplainPO> complainRecordPOS = dao.select(complainRecordPO);
			act.setOutData("complainRecordPOS", complainRecordPOS);
			act.setOutData("id", id);
			act.setOutData("isAdd", false);
			act.setForword(CUSTOMER_COMPLAIN_ADD);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户抱怨登记跟进");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void customerComplainCheck(){
		try{	
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			TtCustomerComplainPO complainPO = new TtCustomerComplainPO();
			complainPO.setId(new Long(id));
			
			List <TtCustomerComplainPO> complainPOS = dao.select(complainPO);
			act.setOutData("complainPOS", complainPOS.get(0));
			
			TtCustomerComplainRecordPO complainRecordPO = new TtCustomerComplainRecordPO();
			complainRecordPO.setDetailId(new Long(id));
			List <TtCustomerComplainPO> complainRecordPOS = dao.select(complainRecordPO);
			act.setOutData("complainRecordPOS", complainRecordPOS);
			act.setOutData("id", id);
			act.setOutData("isCheck", true);
			act.setForword(CUSTOMER_COMPLAIN_ADD);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户抱怨登记查看");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void customerComplainInsert(){
		try{	
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String guzhangMiaosu = CommonUtils.checkNull(request.getParamValue("GUZHANG_MIAOSU"));
			String createBy = CommonUtils.checkNull(request.getParamValue("create_by"));
			String chexing = CommonUtils.checkNull(request.getParamValue("model_name"));
			String color = CommonUtils.checkNull(request.getParamValue("color"));
			String address = CommonUtils.checkNull(request.getParamValue("address"));
			String cheliangYongtu = CommonUtils.checkNull(request.getParamValue("car_use_desc"));
			String peizhi = CommonUtils.checkNull(request.getParamValue("package_name"));
			String chezhuDizhi = CommonUtils.checkNull(request.getParamValue("deliverer_adress"));
			String chexi = CommonUtils.checkNull(request.getParamValue("series_name"));
			String shengchanRiqi = CommonUtils.checkNull(request.getParamValue("product_date"));
			String VIN = CommonUtils.checkNull(request.getParamValue("vin"));
			String goucheRiqi = CommonUtils.checkNull(request.getParamValue("guarantee_date"));
			String chezhuDianhua = CommonUtils.checkNull(request.getParamValue("main_phone"));
			String time = CommonUtils.checkNull(request.getParamValue("time"));
			String chezhuXingming = CommonUtils.checkNull(request.getParamValue("ctm_name"));
			String pinpai = CommonUtils.checkNull(request.getParamValue("brand_name"));
			String createbyTel = CommonUtils.checkNull(request.getParamValue("createBy_tel"));
			
			String COMPLAINT_LEVEL = CommonUtils.checkNull(request.getParamValue("COMPLAINT_LEVEL"));
			String COMPLAINT_TYPE = CommonUtils.checkNull(request.getParamValue("COMPLAINT_TYPE"));
			
			TtCustomerComplainPO complainPO = new TtCustomerComplainPO();
			complainPO.setAddress(address);
			complainPO.setCheliangYongtu(cheliangYongtu);
			complainPO.setChexi(chexi);
			complainPO.setChexing(chexing);
			complainPO.setChezhuDianhua(chezhuDianhua);
			complainPO.setChezhuDizhi(chezhuDizhi);
			complainPO.setChezhuXingming(chezhuXingming);
			complainPO.setColor(color);
			complainPO.setCreateBy(createBy);
			complainPO.setCreatebyTel(createbyTel);
			complainPO.setCreateDate(new Date());
			complainPO.setDealerId(new Long(logonUser.getDealerId()));
			complainPO.setGoucheRiqi(formatter.parse(goucheRiqi));
			complainPO.setGuzhangMiaosu(guzhangMiaosu);
			complainPO.setId(new Long(SequenceManager.getSequence("")));
			complainPO.setPeizhi(peizhi);
			complainPO.setPinpai(pinpai);
			complainPO.setShengchanRiqi(formatter.parse(shengchanRiqi));
			complainPO.setStatus("1");//已登记
			complainPO.setTime(formatter.parse(time));
			complainPO.setVin(VIN);
			complainPO.setComplaintLevel(new Integer(COMPLAINT_LEVEL));
			complainPO.setComplaintType(new Integer(COMPLAINT_TYPE));
			
			dao.insert(complainPO);
			if(logonUser.getDealerId()!=null){
				act.setOutData("isDealer", true);
			}else{
				act.setOutData("isDealer", false);
			}
			act.setForword(CUSTOMER_COMPLAIN_INIT);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户抱怨登记新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void queryCustomerComplain(){
		act.getResponse().setContentType("application/json");
		try{
			 SGENJIN_DATE = CommonUtils.checkNull(request.getParamValue("SGENJIN_DATE"));
			 EGENJIN_DATE = CommonUtils.checkNull(request.getParamValue("EGENJIN_DATE"));
			 SDENGJI_DATE = CommonUtils.checkNull(request.getParamValue("SDENGJI_DATE"));
			 EDENGJI_DATE = CommonUtils.checkNull(request.getParamValue("EDENGJI_DATE"));
			 COMPLAINT_LEVEL = CommonUtils.checkNull(request.getParamValue("COMPLAINT_LEVEL"));
			 COMPLAINT_TYPE = CommonUtils.checkNull(request.getParamValue("COMPLAINT_TYPE"));
			 guzhang_miaosu = CommonUtils.checkNull(request.getParamValue("guzhang_miaosu"));
			 VIN = CommonUtils.checkNull(request.getParamValue("vin"));
			 status = CommonUtils.checkNull(request.getParamValue("status"));
			 dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			 cmd = CommonUtils.checkNull(request.getParamValue("cmd"));
			 isDealer = CommonUtils.checkNull(request.getParamValue("isDealer"));
			 
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			if(cmd.equals("1")){
				List<Map<String,Object>> mapList = dao.queryCustomerComplain(this,logonUser,999999,curPage).getRecords();
				String[] head={"登记日期","登记人","抱怨日期","地点","故障描述","服务商代码","服务商名称","抱怨类型","抱怨等级"
						,"车主姓名","车主电话","车主地址","VIN","品牌"
						,"车系","车型","配置","颜色","车辆用途"
						,"处理人","跟进日期","处理内容","状态 "};
				List<Map<String, Object>> records = mapList;
				List params=new ArrayList();
				if(records!=null &&records.size()>0){
					for (Map<String, Object> map1 : records) {
						String DENGJI_DATE = BaseUtils.checkNull(map1.get("DENGJI_DATE"));
						String dengji_ren = BaseUtils.checkNull(map1.get("DENGJI_REN"));
						String TIME = BaseUtils.checkNull(map1.get("BAOYUAN_SHIJIAN"));
						String ADDRESS = BaseUtils.checkNull(map1.get("ADDRESS"));
						String GUZHANG_MIAOSU = BaseUtils.checkNull(map1.get("GUZHANG_MIAOSU"));
						String DEALER_CODE = BaseUtils.checkNull(map1.get("DEALER_CODE"));
						String DEALER_NAME = BaseUtils.checkNull(map1.get("DEALER_NAME"));
						String COMPLAINT_TYPE = BaseUtils.checkNull(map1.get("COMPLAINT_TYPE_NAME"));
						String COMPLAINT_LEVEL = BaseUtils.checkNull(map1.get("COMPLAINT_LEVEL_NAME"));
						String CHEZHU_XINGMING = BaseUtils.checkNull(map1.get("CHEZHU_XINGMING"));
						String CHEZHU_DIANHUA = BaseUtils.checkNull(map1.get("CHEZHU_DIANHUA"));
						String CHEZHU_DIZHI = BaseUtils.checkNull(map1.get("CHEZHU_DIZHI"));
						String VIN = BaseUtils.checkNull(map1.get("VIN"));
						String PINPAI = BaseUtils.checkNull(map1.get("PINPAI"));
						String CHEXI = BaseUtils.checkNull(map1.get("CHEXI"));
						String CHEXING = BaseUtils.checkNull(map1.get("CHEXING"));
						String PEIZHI = BaseUtils.checkNull(map1.get("PEIZHI"));
						String COLOR = BaseUtils.checkNull(map1.get("COLOR"));
						String CHELIANG_YONGTU = BaseUtils.checkNull(map1.get("CHELIANG_YONGTU"));
						String chuli_ren = BaseUtils.checkNull(map1.get("CHULI_REN"));
						String GENJIN_SHIJIAN = BaseUtils.checkNull(map1.get("GENJIN_SHIJIAN"));
						String CHULI_NEIRONG = BaseUtils.checkNull(map1.get("CHULI_NEIRONG"));
						String STATUS = BaseUtils.checkNull(map1.get("STATUS_NAME"));
						String[] detail={DENGJI_DATE,dengji_ren,TIME,ADDRESS,GUZHANG_MIAOSU
								,DEALER_CODE,DEALER_NAME,COMPLAINT_TYPE,COMPLAINT_LEVEL,CHEZHU_XINGMING,CHEZHU_DIANHUA
								,CHEZHU_DIZHI,VIN,PINPAI,CHEXI,CHEXING,PEIZHI
								,COLOR,CHELIANG_YONGTU,chuli_ren,GENJIN_SHIJIAN,CHULI_NEIRONG,STATUS};
						params.add(detail);
					}
				}
				ApplicationDao application = new ApplicationDao();
				application.toExcel(act, head, params,null,"客户抱怨登记明细导出");
			}else{
				PageResult<Map<String,Object>> complaintAcceptData = dao.queryCustomerComplain(this,logonUser,15,curPage);
				act.setOutData("ps", complaintAcceptData);
			}
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户抱怨登记明细导出");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void genjin(){
		try{	
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			String chulishijian = CommonUtils.checkNull(request.getParamValue("chuliTime1"));
			String CREATE_BY = CommonUtils.checkNull(request.getParamValue("chuliren1"));
			String chulineirong = CommonUtils.checkNull(request.getParamValue("chuli_jilu1"));
			
			TtCustomerComplainRecordPO complainRecordPO = new TtCustomerComplainRecordPO();
			
			complainRecordPO.setChuliNeirong(chulineirong);
			complainRecordPO.setDetailId(new Long(id));
			complainRecordPO.setTime(formatter.parse(chulishijian));
			complainRecordPO.setId(new Long(SequenceManager.getSequence("")));
			complainRecordPO.setStatus("2");
			complainRecordPO.setCreateBy(CREATE_BY);
			complainRecordPO.setCreateDate(new Date());
			
			dao.insert(complainRecordPO);
			
			TtCustomerComplainPO complainPO = new TtCustomerComplainPO();
			TtCustomerComplainPO complainPO1 = new TtCustomerComplainPO();
			complainPO.setId(new Long(id));
			
			complainPO1.setStatus("2");
			
			dao.update(complainPO,complainPO1);
			if(logonUser.getDealerId()!=null){
				act.setOutData("isDealer", true);
			}else{
				act.setOutData("isDealer", false);
			}
			act.setForword(CUSTOMER_COMPLAIN_INIT);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户抱怨跟进");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void bihuan(){
		try{	
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String id = CommonUtils.checkNull(request.getParamValue("id"));
			String chulishijian = CommonUtils.checkNull(request.getParamValue("chuliTime1"));
			String CREATE_BY = CommonUtils.checkNull(request.getParamValue("chuliren1"));
			String chulineirong = CommonUtils.checkNull(request.getParamValue("chuli_jilu1"));
			
			TtCustomerComplainRecordPO complainRecordPO = new TtCustomerComplainRecordPO();
			
			complainRecordPO.setChuliNeirong(chulineirong);
			complainRecordPO.setDetailId(new Long(id));
			complainRecordPO.setTime(formatter.parse(chulishijian));
			complainRecordPO.setId(new Long(SequenceManager.getSequence("")));
			complainRecordPO.setStatus("3");
			complainRecordPO.setCreateBy(CREATE_BY);
			complainRecordPO.setCreateDate(new Date());
			
			dao.insert(complainRecordPO);
			
			TtCustomerComplainPO complainPO = new TtCustomerComplainPO();
			TtCustomerComplainPO complainPO1 = new TtCustomerComplainPO();
			complainPO.setId(new Long(id));
			
			complainPO1.setStatus("3");
			
			dao.update(complainPO,complainPO1);
			if(logonUser.getDealerId()!=null){
				act.setOutData("isDealer", true);
			}else{
				act.setOutData("isDealer", false);
			}
			act.setForword(CUSTOMER_COMPLAIN_INIT);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户抱怨闭环");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void showInfoByVin(){
		try{	
			if(dao.checkExist(request)){
				act.setOutData("info", "exist");
			}else{
				act.setOutData("info", dao.showInfoByVin(request));
			}
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询车主信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public String getSGENJIN_DATE() {
		return SGENJIN_DATE;
	}
	public void setSGENJIN_DATE(String sGENJIN_DATE) {
		SGENJIN_DATE = sGENJIN_DATE;
	}
	public String getEGENJIN_DATE() {
		return EGENJIN_DATE;
	}
	public void setEGENJIN_DATE(String eGENJIN_DATE) {
		EGENJIN_DATE = eGENJIN_DATE;
	}
	public String getSDENGJI_DATE() {
		return SDENGJI_DATE;
	}
	public void setSDENGJI_DATE(String sDENGJI_DATE) {
		SDENGJI_DATE = sDENGJI_DATE;
	}
	public String getEDENGJI_DATE() {
		return EDENGJI_DATE;
	}
	public void setEDENGJI_DATE(String eDENGJI_DATE) {
		EDENGJI_DATE = eDENGJI_DATE;
	}
	public String getCOMPLAINT_LEVEL() {
		return COMPLAINT_LEVEL;
	}
	public void setCOMPLAINT_LEVEL(String cOMPLAINT_LEVEL) {
		COMPLAINT_LEVEL = cOMPLAINT_LEVEL;
	}
	public String getCOMPLAINT_TYPE() {
		return COMPLAINT_TYPE;
	}
	public void setCOMPLAINT_TYPE(String cOMPLAINT_TYPE) {
		COMPLAINT_TYPE = cOMPLAINT_TYPE;
	}
	public String getGuzhang_miaosu() {
		return guzhang_miaosu;
	}
	public void setGuzhang_miaosu(String guzhang_miaosu) {
		this.guzhang_miaosu = guzhang_miaosu;
	}
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getIsDealer() {
		return isDealer;
	}
	public void setIsDealer(String isDealer) {
		this.isDealer = isDealer;
	}
}
