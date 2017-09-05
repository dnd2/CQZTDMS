package com.infodms.dms.actions.afterSales.pinApply;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.afterSales.PinApplyDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsPinApplyPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

/**2016-07-13
 * 首保action
 * @author baby_liu_sw
 */
public class PinApplyAction {
	private Logger logger = Logger.getLogger(this.getClass());
	private ActionContext act=ActionContext.getContext();
	private RequestWrapper request=act.getRequest();
	private static POFactory fac=POFactoryBuilder.getInstance();
	PinApplyDAO dao=PinApplyDAO.getInstance();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private int pageSize=Constant.PAGE_SIZE;//页面显示行数
	
	private String pinList = "/jsp/afterSales/pinApply/pinApplyList.jsp";
	private String pinFactoryList = "/jsp/afterSales/pinApply/pinApplyFactoryList.jsp";
	private String pinAdd = "/jsp/afterSales/pinApply/pinApplyAdd.jsp";
	private String sepin = "/jsp/afterSales/pinApply/pinApply.jsp";
	private String hfpin = "/jsp/afterSales/pinApply/pinApplyFactoryDex.jsp";
	/**
	 * 打开pin码查看申请进行查询
	 */
	public void pinList(){
		try{
		//传入参数
		Map<String,Object> paraMap=new HashMap<String,Object>();
		paraMap.put("dealerdId", logonUser.getDealerId());
		paraMap.put("vin", request.getParamValue("vin"));
		paraMap.put("pinNo", request.getParamValue("pinNo"));
		paraMap.put("creatDate", request.getParamValue("creatDate"));
		paraMap.put("outPlantDate", request.getParamValue("outPlantDate"));
		paraMap.put("status", request.getParamValue("status"));
		paraMap.put("flag", request.getParamValue("flag"));
		String flag=request.getParamValue("flag");
		if("t".equals(flag)){
			//取得结果并返回
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; 
			logger.debug("=====================================aaa");	
			PageResult<Map<String,Object>> ps=dao.pinList(paraMap, curPage, pageSize);
			act.setOutData("ps", ps);
		}else{
			act.setForword(pinList);
		}
		
		} catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * 打开首保厂端进行查询
	 */
	public void pinFactoryList(){
		try{
		//传入参数
			Map<String,Object> paraMap=new HashMap<String,Object>();
			paraMap.put("ftype", "ftype");
			paraMap.put("dealerdId", request.getParamValue("dealerId"));
			paraMap.put("dealer_name", request.getParamValue("dealer_name"));
			paraMap.put("vin", request.getParamValue("vin"));
			paraMap.put("pinNo", request.getParamValue("pinNo"));
			paraMap.put("creatDate", request.getParamValue("creatDate"));
			paraMap.put("outPlantDate", request.getParamValue("outPlantDate"));
			paraMap.put("status", request.getParamValue("status"));
			String flag=request.getParamValue("flag");
		if("t".equals(flag)){
			//取得结果并返回
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; 
			logger.debug("=====================================aaa");	
			PageResult<Map<String,Object>> ps=dao.pinList(paraMap, curPage, pageSize);
			act.setOutData("ps", ps);
		}else{
			act.setForword(pinFactoryList);
		}
		
		} catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}

	 //pin申请提报
	public void pinListAdd(){
		try {
			String flag=request.getParamValue("flag");
			DateFormat format = new SimpleDateFormat("yyMMdd"); 
			String Numberdate = format.format(new Date());
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
			String date = format1.format(new Date());
			if("t".equals(flag)){
				String vin=request.getParamValue("vin");
				String reamk=request.getParamValue("reamk");				
				List<Map<String, Object>> listgnumber=dao.GenerateNumber(logonUser.getDealerId(),date);
				String no="";
				if(listgnumber.size()>0){
					no=listgnumber.get(0).get("PIN_NO").toString();
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
				TtAsPinApplyPO pin = new TtAsPinApplyPO();
				pin.setId(Long.parseLong(SequenceManager.getSequence("")));
				pin.setPinNo("SCPN"+Numberdate+no);//单据编码
				pin.setVin(vin);
				pin.setDealerId(Long.parseLong(logonUser.getDealerId()));//服务站Id	
				pin.setRemark(reamk);
				pin.setStatus(Constant.PIN_APPLY_STATUS_01);
				pin.setCreateBy(logonUser.getUserId());//创建人Id			
				pin.setCreateDate(new Date());
				fac.insert(pin);
				act.setOutData("msg","00");				
			}else
			{
				//服务站信息
				String dealerCode=logonUser.getDealerCode();
				String dealerName="";	
				TmDealerPO d=new TmDealerPO();
				d.setDealerId(Long.parseLong(logonUser.getDealerId()));
				List<TmDealerPO> sed=fac.select(d);
				if(sed.size()>0){
					dealerName=sed.get(0).getDealerName().toString();
				}
				//用户信息
				String userName=logonUser.getName();
				
				act.setOutData("userName",userName);
				act.setOutData("date",date);
				act.setOutData("dealerCode",dealerCode);
				act.setOutData("dealerName",dealerName);
				act.setForword(pinAdd);
			}
		}	catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	
	}
	/**
	 * pin申请上报
	 */
	public void pinReport(){
		try {			
			String id=request.getParamValue("id");
			TtAsPinApplyPO pin = new TtAsPinApplyPO();
			pin.setId(Long.parseLong(id));
			List<TtAsPinApplyPO> spin=fac.select(pin);
			if(spin.size()>0 && !spin.get(0).getStatus().equals(Constant.PIN_APPLY_STATUS_01)){
				act.setOutData("msg", "00");
			}else{
				TtAsPinApplyPO repin = new TtAsPinApplyPO();
				repin.setStatus(Constant.PIN_APPLY_STATUS_02);
				repin.setAuditor(logonUser.getUserId());
				repin.setAuditorTime(new Date());
				fac.update(pin, repin);
				act.setOutData("msg", "01");
			}																					
		}	catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * pin申请上报查询
	 */
	public void sepin(){
		try {			
			String id=request.getParamValue("id");
			Map<String,Object> paraMap=new HashMap<String,Object>();
			paraMap.put("id", id);
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; 
				logger.debug("=====================================aaa");	
				PageResult<Map<String,Object>> ps=dao.pinList(paraMap, curPage, pageSize);
			if(ps.getRecords().size()>0){
				act.setOutData("sepin",ps.getRecords().get(0));			
			}		
			act.setForword(sepin);
		}	catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
	/**
	 * pin申请上报回复
	 */
	public void hfpin(){
		try {		
			String flag=request.getParamValue("flag");
			String id=request.getParamValue("id");
			if("t".equals(flag)){
				String pin_code=request.getParamValue("pin_code");
				String reamk=request.getParamValue("reamk");
				TtAsPinApplyPO pin = new TtAsPinApplyPO();
				pin.setId(Long.parseLong(id));
				TtAsPinApplyPO hfpin = new TtAsPinApplyPO();
				hfpin.setPinCode(pin_code);
				hfpin.setReply(reamk);
				hfpin.setStatus(Constant.PIN_APPLY_STATUS_03);
				fac.update(pin, hfpin);
				act.setOutData("msg", "01");
			}else{
				Map<String,Object> paraMap=new HashMap<String,Object>();
				paraMap.put("id", id);
					Integer curPage = request.getParamValue("curPage") != null ? Integer
							.parseInt(request.getParamValue("curPage"))
							: 1; 
					logger.debug("=====================================aaa");	
					PageResult<Map<String,Object>> ps=dao.pinList(paraMap, curPage, pageSize);
				if(ps.getRecords().size()>0){
					act.setOutData("sepin",ps.getRecords().get(0));			
				}		
				act.setForword(hfpin);
			}			
		}	catch (Exception e) {
			BizException e1= new BizException(act,ErrorCodeConstant.FAILURE_CODE,e.getMessage());
			e.printStackTrace();
			act.setException(e1);
		}
	}
}
