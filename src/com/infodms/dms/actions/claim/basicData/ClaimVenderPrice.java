package com.infodms.dms.actions.claim.basicData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.basicData.ClaimVenderPriceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TtDealerChangeInfoPO;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartMakerRelationPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 *   供应商索赔价格维护
 *  2013-4-7 09:34
 * @author KFQ
 *
 */
public class ClaimVenderPrice {
	private Logger logger = Logger.getLogger(ClaimVenderPrice.class);
	ClaimVenderPriceDao dao = ClaimVenderPriceDao.getInstance();
	private final String MAIN_URL = "/jsp/claim/basicData/partBuyPriceQuery.jsp" ;
	private final String MAIN_URLS = "/jsp/claim/basicData/partBuyPriceQueryForExport.jsp" ;
	private final String MAIN_URLa = "/jsp/claim/basicData/partBuyPriceModify.jsp" ;
	private final String MAIN_URLp = "/jsp/claim/basicData/partReserModify.jsp" ;
	private final String SHOW_USER = "/jsp/claim/basicData/showUser.jsp" ;
	private final String SHOW_RESER = "/jsp/claim/basicData/showReser.jsp" ;
	private final String DEALER_INIT_VIN="/jsp/claim/basicData/dealerInitVinSetMain.jsp";
	
	//经销商是否阿可以手动输入VIN 维护
	public void dealerInitVinSet(){
		ActionContext act = ActionContext.getContext();
		AclUserBean user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(DEALER_INIT_VIN);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商手动输入VIN维护");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//经销商查询
	public void dealerQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String dealerCode = request.getParamValue("DEALER_CODE");
			String dealerName = request.getParamValue("DEALER_NAME");
			String isScan = request.getParamValue("IS_SCAN");
			if(Constant.IF_TYPE_YES.toString().equalsIgnoreCase(isScan)){
				isScan="1";
			}else if(Constant.IF_TYPE_NO.toString().equalsIgnoreCase(isScan)){
				isScan="0";
			}
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 10 ;
			PageResult<Map<String,Object>> ps = dao.getDealerList(dealerCode, dealerName, isScan, pageSize, curPage);
			act.setOutData("ps", ps);
			
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"经销商手动输入VIN");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//状态修改
	@SuppressWarnings("unchecked")
	public void dealerModefy(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			String type = request.getParamValue("type");
			int isScan =0;
			if("1".equalsIgnoreCase(type)){
				isScan=0;
			}else if("0".equalsIgnoreCase(type)){
				isScan=1;
			}
			TmDealerPO d = new TmDealerPO();
			TmDealerPO d2 = new TmDealerPO();
			d.setDealerId(Long.valueOf(id));
			d2.setIsScan(isScan);
			dao.update(d, d2);
			
			//保存操作明细
			TtDealerChangeInfoPO p = new TtDealerChangeInfoPO();
			p.setId(Long.parseLong(SequenceManager.getSequence("")));
			p.setCreateBy(logonUser.getUserId());
			p.setCreateDate(new Date());
			p.setUpdateDealer(Long.valueOf(id));
			p.setUpdateType(isScan);
			dao.insert(p);
			
			act.setOutData("success", "true");
			
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"经销商手动输入VIN");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void claimVenderPriceInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(MAIN_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商索赔价格维护");
			logger.error(user, be);
			act.setException(be);
		}
	}
	public void claimVenderPriceExport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean user = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(MAIN_URLS);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商索赔价格查询");
			logger.error(user, be);
			act.setException(be);
		}
	}
	//维护查询
	public  void claimVenderPriceQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String part_code = request.getParamValue("part_code");
			String part_oldCode = request.getParamValue("part_oldCode");
			String vender_name = request.getParamValue("vender_name");
			String vender_code = request.getParamValue("vender_code");
			String part_name = request.getParamValue("part_name");
			String spy_name = request.getParamValue("spy_name");
			String status = request.getParamValue("STATUS");
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 10 ;
			PageResult<Map<String,Object>> ps = dao.getClaimList( part_code, spy_name, part_oldCode, vender_name, vender_code,part_name,status,  pageSize, curPage);
			act.setOutData("ps", ps);
			
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"供应商索赔价格维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
//查询功能查询
	public void claimVenderPriceQueryForUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String part_code = request.getParamValue("part_code");
			String part_oldCode = request.getParamValue("part_oldCode");
			String vender_name = request.getParamValue("vender_name");
			String vender_code = request.getParamValue("vender_code");
			String part_name = request.getParamValue("part_name");
			String spy_name = request.getParamValue("spy_name");
			String status = request.getParamValue("STATUS");
			int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			int pageSize = 10 ;
			PageResult<Map<String,Object>> ps = dao.getClaimList( part_code, spy_name, part_oldCode, vender_name,vender_code,part_name,status,  pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
			"供应商索赔价格维护");
	logger.error(logonUser,e1);
	act.setException(e1);
		}
	}
	
	/**
	 * 下载导出供应商索赔价格
	 */
	@SuppressWarnings("unchecked")
	public void exportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String part_code = request.getParamValue("part_code");
			String part_oldCode = request.getParamValue("part_oldCode");
			String vender_name = request.getParamValue("vender_name");
			String vender_code = request.getParamValue("vender_code");
			String part_name = request.getParamValue("part_name");
			String spy_name = request.getParamValue("spy_name");
			String status = request.getParamValue("STATUS");
			String[] head=new String[9];
			head[0]="件号";
			head[1]="配件编码";
			head[2]="配件名称";
			head[3]="供应商编码";
			head[4]="供应商名称";
			head[5]="供应商索赔价格";
			head[6]="索赔员";
			head[7]="新增日期";
			head[8]="是否有效";
		
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getClaimList( part_code, spy_name, part_oldCode, vender_name, vender_code, part_name,status,99999, curPage);
			List<Map<String, Object>> list= ps.getRecords(); 
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
					String[]detail=new String[9];
					detail[0]=(String) map.get("PART_CODE");
					detail[1]=(String) map.get("PART_OLDCODE");
					detail[2]=(String) map.get("PART_NAME");
					detail[3]=(String) map.get("VENDER_CODE");
					detail[4]=(String) map.get("VENDER_NAME");
					detail[5]=String.valueOf(map.get("CLAIM_PRICE"));
					//detail[5]="22";
					detail[6]=(String) map.get("SPY_NAME");
					detail[7]=(String) map.get("CLAIM_DATE");
					String str  = String.valueOf(map.get("STATUS")) ;
					if(str.equals("10011001")){
						detail[8]="有效";
					}else if(str.equals("10011002")){
						detail[8]="无效";
					}
					
					list1.add(detail);
			      }
				com.infodms.dms.actions.claim.basicData.ToExcel.toExceVender(ActionContext.getContext().getResponse(), request, head, list1);
			    }else{
			    	String[]detail=new String[9];
					detail[0]="";
					detail[1]="";
					detail[2]="";
					detail[3]="";
					detail[4]="";
					detail[5]="";
					detail[6]="";
					detail[7]="";
					detail[8]="";
					list1.add(detail);
			    	com.infodms.dms.actions.claim.basicData.ToExcel.toExceVender(ActionContext.getContext().getResponse(), request, head, list1);
			    }
		    act.setForword(MAIN_URLS);	
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供应商索赔价格维护下载");
				logger.error(logonUser,e1);
				act.setException(e1);
			}		
		}
	//修改跳转页面
	@SuppressWarnings("unchecked")
	public void ExchangPrice(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			List list = dao.getClaimList(Long.valueOf(id));
			Map map =(Map)list.get(0);
			request.setAttribute("ID", id);
			request.setAttribute("RESULT", map);
			act.setForword(MAIN_URLa);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
			"供应商索赔价格维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 更新数据
	 */
	@SuppressWarnings("unchecked")
	public void updatePrice(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			String PART_XS = request.getParamValue("PART_XS");
			String LABOUR_XS = request.getParamValue("LABOUR_XS");
			String user_name = request.getParamValue("SPY_NAME");
			//System.out.println(user_id+",,,"+user_name);
			TtPartMakerRelationPO rp = new TtPartMakerRelationPO();
			TtPartMakerRelationPO rp2 = new TtPartMakerRelationPO();
			rp.setRelaionId(Long.valueOf(id));
			rp2.setPartXs(Double.valueOf(PART_XS));
			rp2.setSpyBy(logonUser.getUserId());
			//rp2.setClaimBy(logonUser.getUserId());
			rp2.setClaimDate(new Date());
			rp2.setLabourXs(Double.valueOf(LABOUR_XS));
			dao.update(rp, rp2);
			act.setOutData("success", "true");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
			"供应商索赔系数维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 索赔员查询初始化
	 */
	public void showUser(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act = ActionContext.getContext() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(SHOW_USER);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商索赔价格维护");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/**
	 * 索赔员查询
	 */
	public void showUserQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act = ActionContext.getContext() ;
		RequestWrapper req = act.getRequest();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.getResponse().setContentType("application/json");
			String name = req.getParamValue("USER_NAME");
			String code = req.getParamValue("USER_CODE");
			int pageSize = 15 ;
			int curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getClaimUser(name, code, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔基本参数设定->查询");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/**
	 * 旧件库区库位查询
	 */
	public void oldPartReserQuery(){
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try{
				RequestWrapper request = act.getRequest();
				String part_code = request.getParamValue("part_code");
				String part_name = request.getParamValue("part_name");
				String local_war_house = request.getParamValue("local_war_house");
				String local_war_shel = request.getParamValue("local_war_shel");
				String local_war_layer = request.getParamValue("local_war_layer");
				int curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				int pageSize = 10 ;
				PageResult<Map<String,Object>> ps = dao.getOldPartList( part_code,part_name, local_war_house, local_war_shel, local_war_layer, pageSize, curPage);
				act.setOutData("ps", ps);
			} catch (Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
				"旧件库区库位");
		logger.error(logonUser,e1);
		act.setException(e1);
			}
	}
	//跳转修改页面
	@SuppressWarnings("unchecked")
	public void ExchangeReser(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			System.out.println("有没有得到ID"+id);
			List<Map<String,Object>> list = dao.getReserList(Long.valueOf(id));
			Map map =list.get(0);
			request.setAttribute("ID", id);
			request.setAttribute("RESULT", map);
			act.setForword(MAIN_URLp);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
			"库区库位维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 库区库位查询初始化
	 */
	public void showReser(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act = ActionContext.getContext() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String nameType = request.getParamValue("NAME_TYPE");
		request.setAttribute("NAME_TYPE", nameType);
		try{
			act.setForword(SHOW_RESER);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件库区库位维护");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}
	/**
	 * 库区库位查询
	 */
	public void showReserQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act = ActionContext.getContext() ;
		RequestWrapper req = act.getRequest();
		req = act.getRequest() ;
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			act.getResponse().setContentType("application/json");
			String nameType = req.getParamValue("NAME_TYPE");
			String codeOld = req.getParamValue("CODE_OLD");
			String nameOld = req.getParamValue("NAME_OLD");
			int pageSize = 15 ;
			int curPage = req.getParamValue("curPage") != null ? Integer.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getOLdReser(codeOld, nameOld,nameType, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "库区库位参数设定->查询");
			logger.error(logonUser, be);
			act.setException(be);
		}
	}	
	/**
	 * 更新库区库位
	 */
	@SuppressWarnings("unchecked")
	public void updateReser(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String partCode = request.getParamValue("PART_CODE");
			String warHouse = request.getParamValue("LOCAL_WAR_HOUSE");
			String warShel = request.getParamValue("LOCAL_WAR_SHEL");
			String warLayer = request.getParamValue("LOCAL_WAR_LAYER");
			System.out.println(partCode+",,,"+warHouse);
			TmPtPartBasePO tp = new TmPtPartBasePO();
			TmPtPartBasePO tp2 = new TmPtPartBasePO();
			tp.setPartCode(String.valueOf(partCode));
			tp2.setLocalWarHouse(String.valueOf(warHouse));
			tp2.setLocalWarShel(String.valueOf(warShel));
			tp2.setLocalWarLayer(String.valueOf(warLayer));
			tp2.setUpdateDate(new Date());
			dao.update(tp, tp2);
			act.setOutData("success", "true");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
			"旧件库区库位维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
}
