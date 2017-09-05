package com.infodms.dms.actions.claim.oldPart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import sun.reflect.generics.tree.ReturnType;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.oldPart.ClaimoldPartTransportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOldpartTransportDetailPO;
import com.infodms.dms.po.TmOldpartTransportPO;
import com.infodms.dms.po.TtAsCreateOldPo;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：索赔旧件管理--回运清单维护
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class ClaimOldPartTransportManager extends BaseAction{
	public Logger logger = Logger.getLogger(ClaimBackPieceBackListOrdManager.class);
	String transportIndexUrl = "/jsp/claim/oldPart/oldPartTransportIndex.jsp";
	String transpottDeatailInit = "/jsp/claim/oldPart/oldPartTransportDetailInit.jsp";
	String transportAddUrl = "/jsp/claim/oldPart/oldPartTransportAdd.jsp";
	String transportDetailUrl = "/jsp/claim/oldPart/oldPartTransportDetail.jsp";
	String transportModifyUrl = "/jsp/claim/oldPart/oldPartTransportModify.jsp";
	private final String queryOldCreateType= "/jsp/claim/oldPart/queryOldCreateType.jsp";
	private final String oldTypeCreateAdd= "/jsp/claim/oldPart/oldTypeCreateAdd.jsp";
	private final String oldCreateUpdate= "/jsp/claim/oldPart/oldCreateUpdate.jsp";
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUserBean = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request=act.getRequest();
	private ClaimoldPartTransportDao dao=ClaimoldPartTransportDao.getInstance();
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
	public void queryOldpartTransport(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String COMMAND = request.getParamValue("COMMAND");
			if(COMMAND!=null){
				String dealerCode = logonUser.getDealerCode();
				//查询条件
			    String transportNo = request.getParamValue("TRANSPORT_NO");//运输公司名称
			    String transportStatus = request.getParamValue("TRANSPORT_STATUS");//运输公司状态
			    PageResult<Map<String, Object>> ps = null;
				ps = dao.queryOldPartTransport(request,dealerCode,transportNo,transportStatus, getCurrPage(), Constant.PAGE_SIZE);
				request.setAttribute("DEALER_CODE", dealerCode);
				act.setOutData("ps", ps);
				act.setForword(transportIndexUrl);
			}else{
				request.setAttribute("DEALER_CODE", logonUser.getDealerCode());
				act.setForword(transportIndexUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void	ExportOldpartTransport(){
		PageResult<Map<String, Object>> ps = null;
		//查询条件
	    String transportNo = request.getParamValue("TRANSPORT_NO");//运输公司名称
	    String transportStatus = request.getParamValue("TRANSPORT_STATUS");//运输公司状态
		dao.ExportOldpartTransport(act,request, Constant.PAGE_SIZE_MAX,getCurrPage());
		
	}
	
	public void queryOldpartTransportDetailInit(){
		try {
			act=ActionContext.getContext();
			act.setForword(transpottDeatailInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单查询初始化");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void queryOldpartTransportDetail(){
		try{
			act=ActionContext.getContext();
			//查询条件
		    String transportNo = request.getParamValue("TRANSPORT_NO");//运输公司名称
		    String transportStatus = request.getParamValue("TRANSPORT_STATUS");//运输状态
		    String dealerName = request.getParamValue("DEALER_NAME");
		    String dealerCode = request.getParamValue("DEALER_CODE");
		    String status = request.getParamValue("STATUS");
		    
		    PageResult<Map<String, Object>> ps = null;
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1;
			// 查询语句
			ps = dao.queryOldPartTransportDetail(dealerName,dealerCode,transportNo,transportStatus,status,curPage,15);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	
	public void oldpartTransportAdd() {
		String dealerId = logonUser.getDealerId();
		try {
			String COMMAND = request.getParamValue("COMMAND");
			if(COMMAND==null){//新增
				String flag = request.getParamValue("flag");
				String[] transport_name = request.getParamValues("TRANSPORT_NAME");
				String[] link_phone = request.getParamValues("LINK_PHONE");
				String[] arrive_place = request.getParamValues("ARRIVE_PLACE");
				String[] send_person = request.getParamValues("SEND_PERSON");
				String[] send_phone = request.getParamValues("SEND_PHONE");
				String[] arrive_way = request.getParamValues("ARRIVE_WAY");
				String[] price_weight = request.getParamValues("PRICE_WEIGHT");
				String[] price_cubic = request.getParamValues("PRICE_CUBIC");
				String[] price_other = request.getParamValues("PRICE_OTHER");
				String[] send_costs = request.getParamValues("SEND_COSTS");
				String[] return_types = request.getParamValues("RETURN_TYPE");//加了一个回运类型
				String[] remark = request.getParamValues("REMARK");
				TmOldpartTransportPO tot = new TmOldpartTransportPO();
				Long gettransportId = Utility.getLong(SequenceManager.getSequence(""));
				String dealerCode = logonUser.getDealerCode();
				tot.setTransportId(gettransportId);
				tot.setCreateBy(logonUser.getUserId());
				tot.setCreateDate(new Date());
				
				tot.setDealerCode(dealerCode);
				tot.setDealerId(Long.valueOf(dealerId));
				TmDealerPO d = new TmDealerPO();
				d.setDealerCode(dealerCode);
				d = (TmDealerPO) dao.select(d).get(0);
				
				tot.setDealerName(d.getDealerShortname());
				tot.setTransportNo(SequenceManager.getSequence("TO"));
				if("1".equals(flag)){//保存
					
					tot.setTransportStatus(Constant.SP_JJ_TRANSPORT_STATUS_01);
					
				}else{//提交
					tot.setTransportStatus(Constant.SP_JJ_TRANSPORT_STATUS_02);
					tot.setReportDate(new Date());
					tot.setReportUser(logonUser.getUserId());
				}
				dao.insert(tot);
				for(int i =0;i<transport_name.length;i++){
					
					TmOldpartTransportDetailPO totd = new TmOldpartTransportDetailPO();
					totd.setTransportId(gettransportId);
					totd.setArrivePlace(arrive_place[i]);
					totd.setArriveWay(Integer.valueOf(arrive_way[i]));
					totd.setCreateBy(logonUser.getUserId());
					totd.setCreateDate(new Date());
					totd.setDetailId(Utility.getLong(SequenceManager.getSequence("")));
					totd.setLinkPhone(link_phone[i]);
					totd.setPriceCubic(Double.valueOf(price_cubic[i]==null?"0":price_cubic[i]));
					totd.setPriceOther(Double.valueOf(price_other[i]==null?"0":price_other[i]));
					totd.setPriceWeight(Double.valueOf(price_weight[i]==null?"0":price_weight[i]));
					totd.setRemark(remark[i]);
					totd.setSendCosts(Double.valueOf(send_costs[i]==null?"0":send_costs[i]));
					totd.setSendPerson(send_person[i]);
					totd.setSendPhone(send_phone[i]);
					totd.setTransportName(transport_name[i]);
					totd.setReturnType(return_types[i]);
					dao.insert(totd);
				}
				act.setOutData("returnValue", 1);
			}else{//跳转
				
				String dealerCode = logonUser.getDealerCode();
				request.setAttribute("DEALER_CODE", dealerCode);
				TmDealerPO td = new TmDealerPO();
				td.setDealerCode(dealerCode);
				td = (TmDealerPO) dao.select(td).get(0);
				String dealerName = td.getDealerShortname();
				request.setAttribute("DEALER_NAME", dealerName);
				act.setForword(transportAddUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单添加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void oemTransportCheck(){
		try {
			
			String COMMAND = request.getParamValue("COMMAND");
			String dealerCode = logonUser.getDealerCode();
			if(COMMAND!=null){
				
				String transportId = request.getParamValue("TRANSPORT_ID");
				String flag = request.getParamValue("flag");//1,2 用来判断是否是通过还是驳回
				
				String[] ids = request.getParamValues("detailId");
				String auditResult = request.getParamValue("auditResult");
				String[] auditRemark = request.getParamValues("auditRemark");
			    TmOldpartTransportPO tot = new TmOldpartTransportPO();
			    tot.setTransportId(Long.valueOf(transportId));
			    TmOldpartTransportPO totVal = new TmOldpartTransportPO();
			    totVal.setCheckDate(new Date());
			    totVal.setCheckUser(logonUser.getUserId());
			    totVal.setUpdateBy(logonUser.getUserId());
			    totVal.setUpdateDate(new Date());
			    if("1".equals(flag)){
			    	totVal.setTransportStatus(Constant.SP_JJ_TRANSPORT_STATUS_03);
			    }else{
			    	throw new Exception("审核参数异常！");
			    }
			    dao.update(tot, totVal);
			    String[] reu = auditResult.split(",");
			    if(ids!=null){
			    	for(int i=0;i<ids.length;i++){
			    		TmOldpartTransportDetailPO dp = new TmOldpartTransportDetailPO();
			    		TmOldpartTransportDetailPO dp2 = new TmOldpartTransportDetailPO();
			    		dp.setDetailId(Long.valueOf(ids[i]));
			    		dp2.setStatus(Integer.parseInt(reu[i]));
			    		dp2.setAuditRemark(auditRemark[i]);
			    		dao.update(dp, dp2);
			    	}
			    }
			    /*//将该服务站的其他单子作废 目前还原不要此版本
			    TmOldpartTransportPO sp = new TmOldpartTransportPO();
			    sp.setTransportId(Long.valueOf(transportId));
			    sp = (TmOldpartTransportPO) dao.select(sp).get(0);
			    String sql ="UPDATE tm_oldpart_transport a  SET a.transport_status ="+Constant.SP_JJ_TRANSPORT_STATUS_05+" ,a.update_date=SYSDATE,a.update_by="+logonUser.getUserId()+" WHERE a.dealer_id="+sp.getDealerId()+" AND a.transport_id <>"+transportId+" AND a.transport_status not in("+Constant.SP_JJ_TRANSPORT_STATUS_01+","+Constant.SP_JJ_TRANSPORT_STATUS_02+") ";
			    dao.update(sql, null);*/
			    act.setOutData("returnValue", 1);
			}else{
				String viewOrCheck = request.getParamValue("VIEW_OR_CHECK");
				
				String transportId = request.getParamValue("TRANSPORT_ID");
				TmOldpartTransportDetailPO totd = new TmOldpartTransportDetailPO();
				totd.setTransportId(Long.valueOf(transportId));
				List<TmOldpartTransportDetailPO> listBean = dao.select(totd);
				act.setOutData("listBean", listBean);
				
				Map<String,Object> hashMap = dao.queryOldPartTransportById(request, transportId);
				
				request.setAttribute("DEALER_NAME", hashMap.get("DEALER_NAME"));
				request.setAttribute("DEALER_CODE", hashMap.get("DEALER_CODE"));
				request.setAttribute("VIEW_OR_CHECK", viewOrCheck);
				act.setForword(transportDetailUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void modifyTransportDetail(){
		try {
			
			String COMMAND = request.getParamValue("COMMAND");
			String dealerCode = logonUser.getDealerCode();
			if(COMMAND!=null){
				
				Long userId = logonUser.getUserId();
				
				String transportId = request.getParamValue("TRANSPORT_ID");
				String flag = request.getParamValue("flag");
				String[] detail_id = request.getParamValues("DETAIL_ID");
				String[] transport_name = request.getParamValues("TRANSPORT_NAME");
				String[] link_phone = request.getParamValues("LINK_PHONE");
				String[] arrive_place = request.getParamValues("ARRIVE_PLACE");
				String[] send_person = request.getParamValues("SEND_PERSON");
				String[] send_phone = request.getParamValues("SEND_PHONE");
				String[] arrive_way = request.getParamValues("ARRIVE_WAY");
				String[] price_weight = request.getParamValues("PRICE_WEIGHT");
				String[] price_cubic = request.getParamValues("PRICE_CUBIC");
				String[] price_other = request.getParamValues("PRICE_OTHER");
				String[] send_costs = request.getParamValues("SEND_COSTS");
				String[] return_types = request.getParamValues("RETURN_TYPE");//加了一个回运类型
				String[] remark = request.getParamValues("REMARK");
				
				TmOldpartTransportPO tot = new TmOldpartTransportPO();
				tot.setTransportId(Long.valueOf(transportId));
				TmOldpartTransportPO totVal = new TmOldpartTransportPO();
				if("1".equals(flag)){//保存
					
					totVal.setTransportStatus(Constant.SP_JJ_TRANSPORT_STATUS_01);
					totVal.setUpdateBy(userId);
					totVal.setUpdateDate(new Date());
					
				}else{//提交
					totVal.setTransportStatus(Constant.SP_JJ_TRANSPORT_STATUS_02);
					totVal.setReportDate(new Date());
					totVal.setReportUser(userId);
					totVal.setUpdateBy(userId);
					totVal.setUpdateDate(new Date());
				}
				
				dao.update(tot, totVal);
				List<Long> newDetailList = new ArrayList<Long>();
				if(detail_id!=null&&detail_id.length>0){
					for(int i =0;i<detail_id.length;i++){
						
						TmOldpartTransportDetailPO totdVal = new TmOldpartTransportDetailPO();
						totdVal.setArrivePlace(arrive_place[i]);
						totdVal.setArriveWay(Integer.valueOf(arrive_way[i]));
						totdVal.setUpdateBy(userId);
						totdVal.setUpdateDate(new Date());
						totdVal.setLinkPhone(link_phone[i]);
						totdVal.setPriceCubic(Double.valueOf(price_cubic[i]==null?"0":price_cubic[i]));
						totdVal.setPriceOther(Double.valueOf(price_other[i]==null?"0":price_other[i]));
						totdVal.setPriceWeight(Double.valueOf(price_weight[i]==null?"0":price_weight[i]));
						totdVal.setRemark(remark[i]);
						totdVal.setSendCosts(Double.valueOf(send_costs[i]==null?"0":send_costs[i]));
						totdVal.setSendPerson(send_person[i]);
						totdVal.setSendPhone(send_phone[i]);
						totdVal.setReturnType(return_types[i]);
						totdVal.setTransportName(transport_name[i]);
						
						if(detail_id[i]!=null){//存在更新
							TmOldpartTransportDetailPO totd = new TmOldpartTransportDetailPO();
							totd.setDetailId(Long.valueOf(detail_id[i]));
							
							dao.update(totd, totdVal);
						}else{//不存在新增
							Long detailId = Utility.getLong(SequenceManager.getSequence(""));
							newDetailList.add(detailId);
							totdVal.setTransportId(Long.valueOf(transportId));
							totdVal.setDetailId(detailId);
							totdVal.setCreateBy(userId);
							totdVal.setCreateDate(new Date());
							dao.insert(totdVal);
						}
						
					}
				}
				//删除没有保存到运输明细ID
				dao.delOldPartTransport(request, dealerCode, transportId, detail_id,newDetailList.toArray());
				
			    act.setOutData("returnValue", 1);
			    request.setAttribute("DEALER_CODE", dealerCode);
			    act.setForword(transportIndexUrl);
			}else{
				String transportId = request.getParamValue("TRANSPORT_ID");
				TmOldpartTransportDetailPO totd = new TmOldpartTransportDetailPO();
				totd.setTransportId(Long.valueOf(transportId));
				List<TmOldpartTransportDetailPO> listBean = dao.select(totd);
				act.setOutData("listBean", listBean);
				
				TmOldpartTransportPO po = new TmOldpartTransportPO();
				po.setTransportId(Long.valueOf(transportId));
				List<TmOldpartTransportPO> list = dao.select(po);
				
				if(list != null && list.size() == 1) {
					request.setAttribute("DEALER_NAME", list.get(0).getDealerName());
				}
				
				request.setAttribute("DEALER_CODE", dealerCode);
				act.setForword(transportModifyUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void changeTransportStatus(){
		try {
			
			String transportId = request.getParamValue("TRANSPORT_ID");
			String transportStatus = request.getParamValue("TRANSPORT_STATUS");
			
			
		    TmOldpartTransportPO tot = new TmOldpartTransportPO();
		    Long transportid = Long.valueOf(transportId);
			tot.setTransportId(transportid);
		    TmOldpartTransportPO totVal = new TmOldpartTransportPO();
		    totVal.setUpdateBy(logonUser.getUserId());
		    totVal.setUpdateDate(new Date());
		    totVal.setTransportStatus(Integer.valueOf(transportStatus));
		    if(Constant.SP_JJ_TRANSPORT_STATUS_02.toString().equals(transportStatus)){
		    	totVal.setReportDate(new Date());
		    	totVal.setReportUser(logonUser.getUserId());
		    }
		    if(Constant.SP_JJ_TRANSPORT_STATUS_05.toString().equals(transportStatus)){//失效zyw 2015-3-27
		    	TmOldpartTransportDetailPO t1=new TmOldpartTransportDetailPO();
		    	t1.setTransportId(transportid);
		    	TmOldpartTransportDetailPO t2=new TmOldpartTransportDetailPO();
		    	t2.setStatus(3);
		    	dao.update(t1, t2);
		    }
		    dao.update(tot, totVal);
		    request.setAttribute("DEALER_CODE", logonUser.getDealerCode());
		    act.setForword(transportIndexUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询旧件类型的列表跳转
	 */
	public void queryOldCreateTypeInit(){
		this.sendForwordMsg(queryOldCreateType, "查询旧件类型的列表跳转");
	}
	
	/**
	 * 查询旧件类型的列表
	 */
	public void queryOldCreateType(){
		String  oldcode=request.getParamValue("codeold");
		String typename=request.getParamValue("selectType");
		String nameold = request.getParamValue("nameold");
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps =dao.queryCreateOldPartType(Integer.parseInt(typename)==-1?null:typename,oldcode,nameold,curPage, 15);
		act.setOutData("ps", ps);
		this.sendForwordMsg(queryOldCreateType, "查询旧件类型的列表");
	}
	/**
	 * 提交页面之后返回查询页面
	 */
	public void oldTypeaddCommit(){
			try {
				act = ActionContext.getContext();
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				String[] nameTypes = request.getParamValues("nameType");
				String[] nameOlds = request.getParamValues("nameOld");
				String[] codeOlds = request.getParamValues("codeOld");
				int temp=0;
				for (String codeOld : codeOlds) {
					String nameType = nameTypes[temp];
					String nameOld = nameOlds[temp];
					Long id = Utility.getLong(SequenceManager.getSequence(""));
					TtAsCreateOldPo po=new TtAsCreateOldPo(id,Integer.parseInt(nameType),codeOld.toUpperCase(),nameOld,logonUser.getUserId().toString(),new Date());
					dao.insert(po);
					temp++;
				}
				this.sendForwordMsg(queryOldCreateType, "提交页面");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	/**
	 * 添加页面
	 */
	public void oldTypeCreateAdd(){
		this.sendForwordMsg(oldTypeCreateAdd, "添加页面");
	}
	/**
	 * 修改页面
	 */
	public void oldCreateUpdate(){
		String id = request.getParamValue("id");
		List<TtAsCreateOldPo> list = dao.select(new TtAsCreateOldPo(Long.parseLong(id)));
		TtAsCreateOldPo po = list.get(0);
		request.setAttribute("po", po);
		this.sendForwordMsg(oldCreateUpdate, "修改页面");
	}
	/**
	 * 修改页面提交
	 */
	public void oldTypeaddUpdateCommit(){
		String id = request.getParamValue("id");
		String nameOld = request.getParamValue("nameOld");
		String sql=" update tt_as_create_old t set t.name_Old='"+nameOld+"' where t.id="+id;
		dao.update(sql, null);
		this.sendForwordMsg(queryOldCreateType, "查询旧件类型的列表跳转");
	}
	
	/**
	 * //先检测数据库里是否有重复的code
	 */
	public void checkCode(){ 
		String[] codeOldChecks = request.getParamValues("codeOld");
		int temp=0;
		for (String codeOld : codeOldChecks) {
			temp=dao.checkCodeunique(codeOld.toUpperCase());
		}
		if(temp>0){
			act.setOutData("kpnum", "系统中里有重复的代码"); 
		}else{
			act.setOutData("kpnum", ""); 
		}
	}
	/**
	 * 公共列表页面跳转——周月威
	 * @param url
	 * @param msg
	 */
	public void sendForwordMsg(String url,String msg){
		try {
		    act.setForword(url);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, msg);
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void updateOldPartTransportStatus( ){
		TmOldpartTransportPO oldpartTransportPO = new TmOldpartTransportPO();
		TmOldpartTransportPO oldpartTransportPO1 = new TmOldpartTransportPO();
		oldpartTransportPO.setTransportId(Long.valueOf(request.getParamValue("TRANSPORT_ID")));
		oldpartTransportPO1.setTransportStatus(Constant.SP_JJ_TRANSPORT_STATUS_05);
		oldpartTransportPO1.setUpdateBy(loginUser.getUserId());
		oldpartTransportPO1.setCheckUser(loginUser.getUserId());
		oldpartTransportPO1.setCheckDate(new Date());
		int res =dao.update(oldpartTransportPO, oldpartTransportPO1);
		act.setOutData("succ", res);
	}
	
	
}
	

 	