package com.infodms.dms.actions.crm.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.data.DataManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class DataManage {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DataManageDao dao = DataManageDao.getInstance();

	private final String DATA_QUERY_URL = "/jsp/crm/data/dataInit.jsp";// 可以维护的数据字典查询(车厂)
	private final String DATA_MODIFY_INIT = "/jsp/crm/data/dataModify.jsp";// 修改数据字典内容(车厂)
	private final String DEALER_DATA_QUERY_URL = "/jsp/crm/data/dataDealerInit.jsp";// 可以维护的数据字典查询(经销商)
	private final String DEALER_DATA_MODIFY_INIT = "/jsp/crm/data/dataDealerModify.jsp";// 修改数据字典内容(经销商)
	
	private final String DATA_SELECT_URL = "/jsp/crm/data/dataSelect.jsp";// 数据字典查询
	/**
	 * 车厂字典维护首页（车厂）
	 */
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			act.setOutData("funcStr", funcStr);
			act.setForword(DATA_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商字典维护首页（经销商）
	 */
	public void doDealerInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
			String codeName = CommonUtils.checkNull(request.getParamValue("codeName"));		//字典名称
				if(codeName !=null && !"".equals(codeName)){
					act.setOutData("codeName", codeName);
				}
			act.setOutData("funcStr", funcStr);
			act.setForword(DEALER_DATA_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	
	 * @param 		:	
	 * @return		:
	 * @throws		:	查询数据字典（车厂）
	 * LastUpdate	:	2010-8-30
	 */
	public void dataQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String status = CommonUtils.checkNull(request.getParamValue("status"));		//经销商用户组的状态
			String code =  CommonUtils.checkNull(request.getParamValue("code"));			//组名称
			String name =  CommonUtils.checkNull(request.getParamValue("name"));			//组名称
			String parName =  CommonUtils.checkNull(request.getParamValue("parName"));			//组名称
			
			Map<String ,String > map=new HashMap<String,String>();
			map.put("status", status);
			map.put("code", code);
			map.put("name", name);
			map.put("parName", parName);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDataQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	
	 * @param 		:	
	 * @return		:
	 * @throws		:	查询数据字典（经销商）
	 * LastUpdate	:	2010-8-30
	 */
	public void dataDealerQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String status = CommonUtils.checkNull(request.getParamValue("status"));		//状态
			String typeName =  CommonUtils.checkNull(request.getParamValue("typeName"));			//类型名称
			String ifDealer =  CommonUtils.checkNull(request.getParamValue("ifDealer"));			//是否可以维护
			Map<String ,String > map=new HashMap<String,String>();
			map.put("status", status);
			map.put("typeName", typeName);
			map.put("dealerId", logonUser.getDealerId());
			map.put("ifDealer", ifDealer);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDataDealerQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * 字典修改界面（车厂）
	 */
	public void dataModifyInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			ActionContext act = ActionContext.getContext();
			try {
				String codeId=CommonUtils.checkNull(request.getParamValue("codeId"));
				TcCodePO tc =new TcCodePO();
				tc.setCodeId(codeId);
				tc=(TcCodePO) dao.select(tc).get(0);
				act.setOutData("tc", tc);
				if(tc.getStatus()!=null&&!"".equals(tc.getStatus().toString())){
					TcCodePO td=new TcCodePO();
					td.setCodeId(tc.getStatus().toString());
					td=(TcCodePO) dao.select(td).get(0);
					act.setOutData("status", td.getCodeDesc().toString());
					
				}
				if(tc.getIfDealer()!=null&&!"".equals(tc.getIfDealer().toString())){
					String ifDealer=CommonUtils.getCodeDesc(tc.getIfDealer().toString());
					act.setOutData("ifDealer", ifDealer);
				}
				if(tc.getIfVisible()!=null&&!"".equals(tc.getIfVisible().toString())){
					String ifVisible=CommonUtils.getCodeDesc(tc.getIfVisible().toString());
					act.setOutData("ifVisible", ifVisible);
				}
				//查询本级的下级
				Map<String,String> map=new HashMap<String,String>();
				map.put("dealerId", logonUser.getDealerId());
				map.put("type", codeId);
				List<Map<String,Object>> tcList=dao.getDataNextList(map);
				act.setOutData("tcList", tcList);
				act.setForword(DATA_MODIFY_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 字典修改界面（经销商）
	 */
	public void dataDealerModifyInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			ActionContext act = ActionContext.getContext();
			try {
				String codeId=CommonUtils.checkNull(request.getParamValue("codeId"));
				TcCodePO tc =new TcCodePO();
				tc.setCodeId(codeId);
				tc=(TcCodePO) dao.select(tc).get(0);
				act.setOutData("tc", tc);
				if(tc.getStatus()!=null&&!"".equals(tc.getStatus().toString())){
					TcCodePO td=new TcCodePO();
					td.setCodeId(tc.getStatus().toString());
					td=(TcCodePO) dao.select(td).get(0);
					act.setOutData("status", td.getCodeDesc().toString());
					
				}
				
				//查询本级的下级
				Map<String,String> map=new HashMap<String,String>();
				map.put("dealerId", logonUser.getDealerId());
				map.put("type", codeId);
				List<Map<String,Object>> tcList=dao.getDataNextList(map);
				TmDealerPO td=new TmDealerPO();
				td.setDealerId(new Long(logonUser.getDealerId()));
				td=(TmDealerPO) dao.select(td).get(0);
				act.setOutData("td", td);
				act.setOutData("tcList", tcList);
				act.setForword(DEALER_DATA_MODIFY_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 字典修改界面（经销商）
	 */
	public void dataSelectInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			ActionContext act = ActionContext.getContext();
			try {
				String codeId=CommonUtils.checkNull(request.getParamValue("codeId"));
				TcCodePO tc =new TcCodePO();
				tc.setCodeId(codeId);
				tc=(TcCodePO) dao.select(tc).get(0);
				act.setOutData("tc", tc);
				//查询本级的下级
				Map<String,String> map=new HashMap<String,String>();
				if("10431001".equals(logonUser.getDutyType())){
					map.put("dealerId", null);
				}else{
					map.put("dealerId", logonUser.getDealerId());
				}
				
				map.put("type", codeId);
				List<Map<String,Object>> tcList=dao.getDataNextList(map);
				act.setOutData("tcList", tcList);
				act.setForword(DATA_SELECT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 保存数据字典数据（车厂）
	 */
	public void saveData(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				//获取本级数据
				String codeId=request.getParamValue("codeId");
				String codeName=request.getParamValue("codeName");
				String status=request.getParamValue("status");
				String ifDealer=request.getParamValue("ifDealer");
				String ifVisible=request.getParamValue("ifVisible");
				String codeLevel=request.getParamValue("codeLevel");
				String[] nextCodeDescArray=request.getParamValues("nextCodeDesc");
				String[]nextCodeIdArray=request.getParamValues("nextCodeId");
				//判断如果将经销商可以维护修改为否就判断是否有下一级经销商维护的数据
				//维护为是的时候就判断车厂是否已经维护
				if("10041002".equals(ifDealer)){
					int flag=CommonUtils.getNextLevelCount(codeId,"2",null);
					if(flag>0){
						act.setOutData("flag", 2);
						return;
					}
				}else{
					int flag=CommonUtils.getNextLevelCount(codeId,"1",null);
					if(flag>0){
						act.setOutData("flag", 3);
						return;
					}
				}
				//修改本级数据
				TcCodePO tp=new TcCodePO();
				tp.setCodeId(codeId);
				TcCodePO tp1=new TcCodePO();
				tp1.setCodeDesc(codeName);
				tp1.setStatus(new Integer(status));
				tp1.setIfDealer(new Long(ifDealer));
				tp1.setIfVisible(new Long(ifVisible));
				tp1.setUpdateBy(logonUser.getUserId());
				tp1.setUpdateDate(new Date());
				dao.update(tp, tp1);
				if(nextCodeDescArray!=null){
					for(int k=0;k<nextCodeDescArray.length;k++){
						TcCodePO tc=new TcCodePO();
						tc.setCodeDesc(nextCodeDescArray[k]);
						tc.setType(codeId);
						tc.setTypeName(codeName);
						tc.setStatus(Constant.STATUS_ENABLE);
						//判断如果存在codeId就修改不存在就新增
						if(nextCodeIdArray[k]==null||"".equals(nextCodeIdArray[k])){
							//新增子集数据
							int nextCount=CommonUtils.getNextLevelCount(codeId,"1",null);
							String firstCodeId="";
							firstCodeId+=codeId+(1000+nextCount);
							Long nextCodeId=Long.parseLong(firstCodeId)+1;
							tc.setCodeId(nextCodeId.toString());
							tc.setCreateBy(logonUser.getUserId());
							tc.setCreateDate(new Date());
							tc.setCodeLevel(Integer.parseInt(codeLevel)+1);
							dao.insert(tc);
						}else{
							TcCodePO tc0=new TcCodePO();
							tc0.setCodeId(nextCodeIdArray[k]);
							tc.setUpdateBy(logonUser.getUserId());
							tc.setUpdateDate(new Date());
							dao.update(tc0, tc);
						}
					}
				}
				
				act.setOutData("flag", 1);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存数据字典数据（经销商）
	 */
	public void saveDealerData(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				//获取本级数据
				String codeLevel=request.getParamValue("codeLevel");
				String codeId=request.getParamValue("codeId");
				String codeName=request.getParamValue("codeName");
				String status=request.getParamValue("status");
				String[] nextCodeDescArray=request.getParamValues("nextCodeDesc");
				String[]nextCodeIdArray=request.getParamValues("nextCodeId");
				//修改本级数据
					TcCodePO tp=new TcCodePO();
					tp.setCodeId(codeId);
					TcCodePO tp1=new TcCodePO();
					tp1.setCodeDesc(codeName);
					tp1.setStatus(new Integer(status));
					tp1.setUpdateBy(logonUser.getUserId());
					tp1.setUpdateDate(new Date());
					dao.update(tp, tp1);
				
				
				if(nextCodeDescArray!=null){
					for(int k=0;k<nextCodeDescArray.length;k++){
						TcCodePO tc=new TcCodePO();
						tc.setCodeDesc(nextCodeDescArray[k]);
						tc.setType(codeId);
						tc.setTypeName(codeName);
						tc.setStatus(Constant.STATUS_ENABLE);
						tc.setIfDealer(new Long(Constant.IF_TYPE_YES));
						tc.setDealerId(new Long(logonUser.getDealerId()));
						//判断如果存在codeId就修改不存在就新增
						if(nextCodeIdArray[k]==null||"".equals(nextCodeIdArray[k])){
							//新增子集数据
							String nextCodeId=SequenceManager.getSequence("");
							tc.setCodeId(nextCodeId);
							tc.setCodeLevel(Integer.parseInt(codeLevel)+1);
							tc.setCreateBy(logonUser.getUserId());
							tc.setCreateDate(new Date());
							dao.insert(tc);
						}else{
							TcCodePO tc0=new TcCodePO();
							tc0.setCodeId(nextCodeIdArray[k]);
							tc.setUpdateBy(logonUser.getUserId());
							tc.setUpdateDate(new Date());
							dao.update(tc0, tc);
						}
					}
				}
				act.setOutData("flag", 1);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 加载下拉列表数据公用的方法只需要传入一个codeId
	 * 
	 */
	public void initData(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String codeId=request.getParamValue("codeId");
				String dealerId=logonUser.getDealerId();
				Map<String,String> map=new HashMap<String,String>();
				map.put("codeId", codeId);
				map.put("dealerId", dealerId);
				List<Map<String,Object>> list=CommonUtils.queryDataList(map);
				act.setOutData("dataList", list);
				act.setOutData("topID", codeId);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 加载省市区的数据
	 * 
	 */
	public void initRegionData(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String codeId=request.getParamValue("codeId");
				Map<String,String> map=new HashMap<String,String>();
				map.put("codeId", codeId);
				List<Map<String,Object>> list=CommonUtils.queryRegionDataList(map);
				act.setOutData("dataList", list);
				act.setOutData("topID", codeId);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 节能惠民加载省市区的数据
	 * 
	 */
	public void initRegionData1(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String codeId=request.getParamValue("codeId");
				Map<String,String> map=new HashMap<String,String>();
				map.put("codeId", codeId);
				List<Map<String,Object>> list=CommonUtils.queryRegionDataList1(map);
				act.setOutData("dataList", list);
				act.setOutData("topID", codeId);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 加载下拉列表数据公用的方法
	 * 
	 */
	public void initALLData(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				
				Map<String,String> map=new HashMap<String,String>();
				map.put("codeId", "0");
				List<Map<String,Object>> list=CommonUtils.queryAllDataList(map);
				act.setOutData("dataList", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
