package com.infodms.dms.actions.claim.basicData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TcCodeBean;
import com.infodms.dms.bean.TreeGuaranteesStrategyBean;
import com.infodms.dms.bean.TtAsWrGameBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.basicData.TreeGuaranteesStrategyDao;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageModelDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrGamefeePO;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.po.TtAsWrRulePO;
import com.infodms.dms.po.TtInvoiceTaxratePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 三包策略维护功能
 * @author XZM
 *
 */
public class TreeGuaranteesStrategy extends BaseAction{
	
	private Logger logger = Logger.getLogger(TreeGuaranteesStrategy.class);
	/** 三包策略查询 首页面 */
	private final String STRATEGY_INIT = "/jsp/claim/basicData/guaranteesStrategyIndex.jsp";
	/** 三包策略查询 添加页面 */
	private final String STRATEGY_ADD = "/jsp/claim/basicData/guaranteesStrategyAdd.jsp";
	/** 三包策略 添加省份、车型和保养费用 页面 */
	private final String STRATEGY_ITEM_ADD = "/jsp/claim/basicData/guaranteesStrategyItemAdd.jsp";
	/** 三包策略 添加保养费用 页面 */
	private final String STRATEGY_AMOUNT_ADD = "/jsp/claim/basicData/guaranteesStrategyAmountAdd.jsp";
	/** 三包策略 车型选择页面 */
	private final String STRATEGY_MODEL_SELECT = "/jsp/claim/basicData/guaranteeShowMaterialGroup.jsp";
	private final String ADD_CODE = "/jsp/claim/basicData/addCode.jsp";
	private final String INVOICE_PROBABILITY_URL = "/jsp/claim/basicData/invoiceProbability.jsp";//主页面（查询）
	private final String INVOICE_ADD_URL = "/jsp/claim/basicData/invoiceProbabilityAdd.jsp";//主页面（查询）
	private ServiceActivityManageModelDao dao = ServiceActivityManageModelDao.getInstance();
	private TreeGuaranteesStrategyDao dao1 = TreeGuaranteesStrategyDao.getInstance();
	/**
	 * 三包策略查询初始化
	 */
	public void strategyQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(this.STRATEGY_INIT);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 三包策略查询
	 */
	public void strategyQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String strategyCode = request.getParamValue("STRATEGY_CODE");//三包策略代码
			String strategyName = request.getParamValue("STRATEGY_NAME");//三包策略名称
			String status = request.getParamValue("STATUS");//三包策略状态
			String gameType = request.getParamValue("GAME_TYPE");//三包策略类型
			String guaranteeCount = request.getParamValue("GUARANTEE_COUNT");//免费保养次数
			String guaranteeRuleCode = request.getParamValue("GUARANTEE_RULE_CODE");//三包规则代码
			String guaranteeRuleName = request.getParamValue("GUARANTEE_RULE_NAME");//三包规则名称
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
			if(companyId==null)
				companyId = new Long(-1);
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			
			TreeGuaranteesStrategyBean conditionVO = new TreeGuaranteesStrategyBean();
			conditionVO.setCompanyId(companyId);
			conditionVO.setGameCode(strategyCode);
			conditionVO.setGameName(strategyName);
		    conditionVO.setStatus(status);
			if(guaranteeCount!=null)
				conditionVO.setMaintaimNum(guaranteeCount);
			conditionVO.setRuleCode(guaranteeRuleCode);
			conditionVO.setRuleName(guaranteeRuleName);
			conditionVO.setCurPage(curPage);
			conditionVO.setPageSize(Constant.PAGE_SIZE);
			conditionVO.setGameType(gameType);
			
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			PageResult<Map<String,Object>> ps = strategyDao.strategyQuery(conditionVO);
			
			act.setOutData("ps", ps);
			
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增三包策略初始化
	 */
	public void addStrategyInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
			
			if(companyId==null)
				companyId = new Long(-1);
			
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			//业务规则
			List<TtAsWrRulePO> dataList = strategyDao.ruleQuery(companyId,Constant.RULE_TYPE_02);
			//系统规则
			List<TtAsWrRulePO> dataList2 = strategyDao.ruleQuery(companyId,Constant.RULE_TYPE_01);
			
			act.setOutData("ruleList", dataList);
			act.setOutData("sysRuleList", dataList2);
			act.setForword(this.STRATEGY_ADD);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增三包策略
	 * 步骤：
	 *     1、保存三包策略信息
	 *     2、保存对应保养费用信息
	 */
	@SuppressWarnings("unchecked")
	public void addStrategy(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
			String strategyCode = request.getParamValue("STRATEGY_CODE");//三包策略代码
			String strategyName = request.getParamValue("STRATEGY_NAME");//三包策略名称
			String guaranteeCount = CommonUtils.checkNull(request.getParamValue("GUARANTEE_COUNT"));//免费保养次数
			String guaranteeRuleId = request.getParamValue("GUARANTEE_RULE_ID");//三包规则ID
			String startTime = request.getParamValue("START_TIME");//三包策略开始时间
			String endTime = request.getParamValue("END_TIME");//三包策略结束时间
			String status = request.getParamValue("STATUS");//三包策略状态
			String gameType = request.getParamValue("GAME_TYPE");//三包策略类型
			String remark = request.getParamValue("REMARK");//备注
			String WR_MONTHS = CommonUtils.checkNull(request.getParamValue("WR_MONTHS"));//三包策略代码
			String WR_MILEAGE = CommonUtils.checkNull(request.getParamValue("WR_MILEAGE"));//三包策略名称
			String CLAIM_MONTH = CommonUtils.checkNull(request.getParamValue("CLAIM_MONTH"));//免费保养次数
			String CLAIM_MELIEAGE = CommonUtils.checkNull(request.getParamValue("CLAIM_MELIEAGE"));//三包规则ID
			String code_id = request.getParamValue("code_id");
			String isForBusi = request.getParamValue("isForBusi");
			String codeDesc = request.getParamValue("codeDesc");
			String code_busi = request.getParamValue("code_busi");
			isForBusi = isForBusi==null?"0":isForBusi;
			boolean flag = true;
			String msg="";
			
			Long id = Long.parseLong(SequenceManager.getSequence(""));
			
			/**
			 * 首先控制选择的性质不能交叉,
			 */
			String[] str = code_busi.split(",");
			String[] str2 = codeDesc.split(",");
			for(int i=0;i<str.length;i++){
				List<Map<String,Object>> list = dao1.checkVehi( str[i], id.toString()) ;
				if(list!=null&&list.size()>0){
					flag = false;
					msg=str2[i]+" 已经在其他策略中存在了!\n";
				}
			}
			TtAsWrGamePO gp = new TtAsWrGamePO();
			gp.setGameCode(strategyCode);
			List<TtAsWrGamePO> gpList = dao1.select(gp);
			if(gpList!=null && gpList.size()>0){
				flag = false;
				msg=msg +"策略代码：【"+strategyCode+"】 已经存在了!\n";
			}
			if(flag){
				String ss = "";
				Set set  = new HashSet();
				String[] codeId = code_id.split(",");
				for(int j=0;j<codeId.length;j++){
					if(set.add(codeId[j])){
						ss=ss+codeId[j]+",";
					}
				}
			TtAsWrGamePO gamePO = new TtAsWrGamePO();
			gamePO.setId(id);
			gamePO.setCompanyId(companyId);
			gamePO.setGameCode(strategyCode);
			gamePO.setGameName(strategyName);
			gamePO.setMaintainNum(guaranteeCount == "" ? 0L : Long.parseLong(guaranteeCount));
			gamePO.setRuleId(Long.parseLong(guaranteeRuleId));
			gamePO.setStartDate(CommonUtils.parseDate(startTime, "yyyy-MM-dd"));
			gamePO.setEndDate(CommonUtils.parseDate(endTime, "yyyy-MM-dd"));
			gamePO.setWrMonth(WR_MONTHS == "" ? 0 : Integer.parseInt(WR_MONTHS));
			gamePO.setWrMelieage(WR_MILEAGE == "" ? 0 : Double.parseDouble(WR_MILEAGE));
			gamePO.setClaimMonth(CLAIM_MONTH == "" ? 0 : Integer.parseInt(CLAIM_MONTH));
			gamePO.setClaimMelieage(CLAIM_MELIEAGE == "" ? 0 : Double.parseDouble(CLAIM_MELIEAGE));
			gamePO.setGameStatus(Integer.parseInt(status));
			gamePO.setGameType(Integer.parseInt(gameType));
			gamePO.setCreateBy(logonUser.getUserId());
			gamePO.setCreateDate(new Date());
			gamePO.setRemark(CommonUtils.checkNull(remark));
			gamePO.setVehicelProperty(ss.substring(0,ss.length()-1));
			gamePO.setIsForBusi(Integer.parseInt(isForBusi));
			gamePO.setVehicleProBusi(code_busi);
			gamePO.setVehicleProBusiDesc(codeDesc);
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			
			//保存策略信息
			strategyDao.insert(gamePO);
			
			//2、保存保养费用信息
			//删除之前设定过保养费用
			strategyDao.deleteStrategyAmount(id);
			
			//添加新保养费用
			if(guaranteeCount!=null && !"".equals(guaranteeCount)){
				Integer count = Integer.parseInt(guaranteeCount);
				for (int i = 0; i < count; i++) {
					String paramname = "GUARANTEE_AMOUNT"+i;
					String amount = request.getParamValue(paramname);
					if(amount!=null){
						TtAsWrGamefeePO feePO = new TtAsWrGamefeePO();
						feePO.setId(Long.parseLong(SequenceManager.getSequence("")));
						feePO.setGameId(id);
						feePO.setMaintainfeeOrder(i+1);
						feePO.setManintainFee(Double.parseDouble(amount));
						feePO.setCreateBy(logonUser.getUserId());
						feePO.setCreateDate(new Date());
						//保存信息
						strategyDao.insert(feePO);
					}
				}
			}
			}
			//	act.setForword(this.STRATEGY_INIT);
				act.setOutData("msg", msg);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询策略对应保养费用
	 */
	public void queryStrategyAmount(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			
			List<TtAsWrGamefeePO> amountList = strategyDao.queryStrategyAmount(Long.parseLong(id));
			
			//查询对应三包策略信息
			TtAsWrGameBean beanPO = strategyDao.queryStrategyById(Long.parseLong(id));
			if(beanPO==null)
				beanPO = new TtAsWrGameBean();
			
			if(beanPO.getMaintainNum()>0){
				if(amountList==null)
					amountList = new ArrayList<TtAsWrGamefeePO>();
				//根据三包规则设定的免费保养次数，将不足的补成空
				for (int i = amountList.size(); i < beanPO.getMaintainNum(); i++) {
					TtAsWrGamefeePO feePO = new TtAsWrGamefeePO();
					feePO.setMaintainfeeOrder(i);
					amountList.add(feePO);
				}
			}
			
			act.setOutData("amountList", amountList);
			act.setOutData("gamePO", beanPO);
			act.setForword(this.STRATEGY_AMOUNT_ADD);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询策略对应的车型信息
	 */
	public void queryStrategyModel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			
			PageResult<Map<String,Object>> ps = strategyDao.queryStrategyModel(Long.parseLong(id),
					curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", ps);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 查询策略对应的身份信息
	 */
	public void queryStrategyProvince(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			
			PageResult<Map<String,Object>> ps = strategyDao.queryStrategyProvince(Long.parseLong(id),
					curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", ps);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 三包策略修改页面初始化
	 * @param id 三包策略ID
	 * @param companyId 所属公司ID
	 * @param logonUser 登陆用户信息
	 * @param act
	 */
	protected void updateStrategyInit(Long id,Long companyId,AclUserBean logonUser,ActionContext act){
		try{
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			//查询对应三包策略信息
			TtAsWrGameBean beanPO = strategyDao.queryStrategyById(id);
			if(beanPO==null)
				beanPO = new TtAsWrGameBean();
			
			List<TtAsWrRulePO> dataList = new ArrayList<TtAsWrRulePO>();
//			if(!Constant.GAME_TYPE_01.equals(beanPO.getGameType())){
//				//查询对应三包规则信息(业务规则)
//				dataList = strategyDao.ruleQuery(companyId,Constant.RULE_TYPE_02);
//			}else{
//				//查询对应三包规则信息(系统规则)
//				dataList = strategyDao.ruleQuery(companyId,Constant.RULE_TYPE_01);
//			}
			dataList = strategyDao.ruleQuery(companyId,Constant.RULE_TYPE_02);
			//查询对应三包策略免费保养费用信息
            List<TtAsWrGamefeePO> amountList = this.getStrategyAmountById(beanPO.getMaintainNum(), id);
			
			act.setOutData("amountList", amountList);
			act.setOutData("ruleList", dataList);
			act.setOutData("gamePO", beanPO);
			act.setForword(this.STRATEGY_ITEM_ADD);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 取得保养费用信息列表，如果没有维护对应次数费用，则对应次数费用为空
	 * @param strategyTime 设定的保养次数
	 * @param id 三包策略ID
	 * @return List<TtAsWrGamefeePO> 保养费用列表
	 */
	private List<TtAsWrGamefeePO> getStrategyAmountById(Long strategyTime,Long id){
		
		TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
		List<TtAsWrGamefeePO> amountList = strategyDao.queryStrategyAmount(id);

		if(strategyTime!=null && strategyTime>0){
			if(amountList==null)
				amountList = new ArrayList<TtAsWrGamefeePO>();
			//根据三包规则设定的免费保养次数，将不足的补成空
			for (int i = amountList.size(); i < strategyTime; i++) {
				TtAsWrGamefeePO feePO = new TtAsWrGamefeePO();
				feePO.setMaintainfeeOrder(i);
				amountList.add(feePO);
			}
		}
		
	   return amountList;
	}
	/**
	 * 打开车辆性质进行增加删除修改
	 */
	
	public void add_code()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			 String code_id= request.getParamValue("code_id");
			 String code_busi= request.getParamValue("code_busi");
			 StringBuffer sql= new StringBuffer();
			// sql.append("SELECT t.CODE_ID,t.CODE_DESC,(select count(*) from TC_CODE C where  C.CODE_ID = t.CODE_ID and  C.CODE_ID in ("+code_id+") ) NUM from TC_CODE t   where t.TYPE = 1072 ");
			 sql.append("SELECT t.CODE_ID,\n");
			 sql.append("              t.code_id||c.code_id code_busi,\n");
			 sql.append("              t.CODE_DESC||'-'||decode(c.code_id,10041001,'营运','非营运')CODE_DESC,\n");
			 sql.append("       case when instr('"+code_busi+"',t.code_id||c.code_id)>0 then 1 else 0 end num\n");
			 sql.append("  from TC_CODE t,tc_code c\n");
			 sql.append(" where t.TYPE = 1072 and c.type=1004"); 

			 List<TcCodeBean> list= dao.select(TcCodeBean.class, sql.toString(), null);
			 act.setOutData("codeList", list);
			 if( list.size() == 0 )
			 {
				 act.setOutData("judetype", 0);
			 }
			 act.setForword(ADD_CODE);

			
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,
					"车辆性质新增出错");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 三包策略修改页面初始化
	 */
	public void updateStrategyInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
			String idStr = request.getParamValue("ID");
			Long id = new Long(-1);
			if(idStr!=null && !"".equals(idStr))
				id = Long.parseLong(idStr);
			
			this.updateStrategyInit(id,companyId, logonUser,act);
			act.setOutData("OPER", "UPDATE");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 修改三包策略基本信息
	 * 步骤：1、先保存策略信息
	 *       2、保存对应保养费用
	 */
	@SuppressWarnings("unchecked")
	public void updateStrategy(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String strategyCode = request.getParamValue("STRATEGY_CODE");//三包策略代码
			String strategyName = request.getParamValue("STRATEGY_NAME");//三包策略名称
			String guaranteeCount = request.getParamValue("GUARANTEE_COUNT");//免费保养次数
			String guaranteeRuleId = request.getParamValue("GUARANTEE_RULE_ID");//三包规则ID
			String startTime = request.getParamValue("START_TIME");//三包策略开始时间
			String endTime = request.getParamValue("END_TIME");//三包策略结束时间
			String status = request.getParamValue("STATUS");//三包策略状态
			String id = request.getParamValue("ID");//三包策略对应ID
			String remark = request.getParamValue("REMARK");//备注
			String WR_MONTHS = CommonUtils.checkNull(request.getParamValue("WR_MONTHS"));//三包策略代码
			String WR_MILEAGE = CommonUtils.checkNull(request.getParamValue("WR_MILEAGE"));//三包策略名称
			String CLAIM_MONTH = CommonUtils.checkNull(request.getParamValue("CLAIM_MONTH")) ;//免费保养次数
			String CLAIM_MELIEAGE = CommonUtils.checkNull(request.getParamValue("CLAIM_MELIEAGE"));//三包规则ID
			String code_id = request.getParamValue("code_id");
			String isForBusi = request.getParamValue("isForBusi");
			String codeDesc = request.getParamValue("codeDesc");
			String code_busi = request.getParamValue("code_busi");
			isForBusi = isForBusi==null?"0":isForBusi;
			boolean flag = true;
			String msg="";
			/**
			 * 首先控制选择的性质不能交叉,
			 */
			String[] str = code_busi.split(",");
			String[] str2 = codeDesc.split(",");
			for(int i=0;i<str.length;i++){
				List<Map<String,Object>> list = dao1.checkVehi( str[i], id) ;
				if(list!=null&&list.size()>0){
					flag = false;
					msg=str2[i]+" 已经在其他策略中存在了!";
					break;
				}
			}
			if(flag){
				String ss = "";
				Set set  = new HashSet();
				String[] codeId = code_id.split(",");
				for(int j=0;j<codeId.length;j++){
					if(set.add(codeId[j])){
						ss=ss+codeId[j]+",";
					}
				}
			TtAsWrGamePO conditionPO = new TtAsWrGamePO();
			conditionPO.setId(Long.parseLong(id));
			
			TtAsWrGamePO gamePO = new TtAsWrGamePO();
			gamePO.setGameCode(strategyCode);
			gamePO.setWrMonth(WR_MONTHS == "" ? 0 : Integer.parseInt(WR_MONTHS));
			gamePO.setWrMelieage(WR_MILEAGE == "" ? 0 : Double.parseDouble(WR_MILEAGE));
			gamePO.setClaimMonth(CLAIM_MONTH == "" ? 0 : Integer.parseInt(CLAIM_MONTH));
			gamePO.setClaimMelieage(CLAIM_MELIEAGE == "" ? 0 : Double.parseDouble(CLAIM_MELIEAGE));
			gamePO.setGameName(strategyName);
			gamePO.setMaintainNum(Long.parseLong(guaranteeCount));
			gamePO.setRuleId(Long.parseLong(guaranteeRuleId));
			gamePO.setStartDate(CommonUtils.parseDate(startTime, "yyyy-MM-dd"));
			gamePO.setEndDate(CommonUtils.parseDate(endTime, "yyyy-MM-dd"));
			gamePO.setGameStatus(Integer.parseInt(status));
			gamePO.setUpdateBy(logonUser.getUserId());
			gamePO.setUpdateDate(new Date());
			gamePO.setRemark(CommonUtils.checkNull(remark));
			gamePO.setVehicelProperty(ss.substring(0, ss.length()-1));
			gamePO.setIsForBusi(Integer.parseInt(isForBusi));
			gamePO.setVehicleProBusi(code_busi);
			gamePO.setVehicleProBusiDesc(codeDesc);
			
			//1、保存策略信息
			dao1.update(conditionPO, gamePO);
			dao1.updateVehicle(id, WR_MONTHS == "" ? "0" : WR_MONTHS);
			
			//2、保存保养费用信息
			//删除之前设定过保养费用
			dao1.deleteStrategyAmount(Long.parseLong(id));
			
			//添加新保养费用
			if(guaranteeCount!=null && !"".equals(guaranteeCount)){
				Integer count = Integer.parseInt(guaranteeCount);
				for (int i = 0; i < count; i++) {
					String paramname = "GUARANTEE_AMOUNT"+i;
					String amount = request.getParamValue(paramname);
					if(amount!=null){
						TtAsWrGamefeePO feePO = new TtAsWrGamefeePO();
						feePO.setId(Long.parseLong(SequenceManager.getSequence("")));
						feePO.setGameId(Long.parseLong(id));
						feePO.setMaintainfeeOrder(i+1);
						feePO.setManintainFee(Double.parseDouble(amount));
						feePO.setCreateBy(logonUser.getUserId());
						feePO.setCreateDate(new Date());
						//保存信息
						dao1.insert(feePO);
					}
				}
			}
			}
		//	act.setForword(this.STRATEGY_INIT);
			act.setOutData("msg", msg);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 保存保养费用
	 * 注：现在保养费用不单独保存，同策略信息同时修改
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public void saveStrategyAmount(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String countStr = request.getParamValue("COUNT");//三包策略维护保养费用次数
			String id = request.getParamValue("ID");//三包策略ID
			
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			//删除之前设定过保养费用
			strategyDao.deleteStrategyAmount(Long.parseLong(id));
			
			//添加新策略
			if(countStr!=null && !"".equals(countStr)){
				Integer count = Integer.parseInt(countStr);
				for (int i = 0; i < count; i++) {
					String paramname = "GUARANTEE_AMOUNT"+i;
					String amount = request.getParamValue(paramname);
					if(amount!=null){
						TtAsWrGamefeePO feePO = new TtAsWrGamefeePO();
						feePO.setId(Long.parseLong(SequenceManager.getSequence("")));
						feePO.setGameId(Long.parseLong(id));
						feePO.setMaintainfeeOrder(i+1);
						feePO.setManintainFee(Double.parseDouble(amount));
						feePO.setCreateBy(logonUser.getUserId());
						feePO.setCreateDate(new Date());
						//保存信息
						strategyDao.insert(feePO);
					}
				}
			}
			
			this.queryStrategyAmount();
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,
					"三包策略维护之保养费用");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 三包策略物料组选择初始
	 */
	@SuppressWarnings("unchecked")
	public void guaranteeGroupListQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
		    String inputId = request.getParamValue("INPUTID");
		    String inputName = request.getParamValue("INPUTNAME");
		    String isMulti = request.getParamValue("ISMULTI");
		    String groupLevel = request.getParamValue("GROUPLEVEL");
		    String STRATEGYID = request.getParamValue("STRATEGYID");
		    List<TmVhclMaterialGroupPO> list = dao.serviceActivityGroupName( STRATEGYID); //车系及子车型组ID
			
			List cxandcxzid = new ArrayList();
			
			for(TmVhclMaterialGroupPO tpo : list){				
				Map map = new HashMap();
				List wrids = null;
				
				 wrids = dao.getXsCxzidsByCxid(tpo.getGroupId());	
				
				map.put("TmVhclMaterialGroupPO", tpo);
				map.put("CXZIDS", wrids);
				cxandcxzid.add(map);
			}
			List<TtAsWrModelGroupPO> cxzlist = dao.getCXZName(); //得到车型组集合
		    act.setOutData("INPUTID", CommonUtils.checkNull(inputId));
		    act.setOutData("INPUTNAME", CommonUtils.checkNull(inputName));
		    act.setOutData("ISMULTI", CommonUtils.checkNull(isMulti));
		    act.setOutData("GROUPLEVEL", CommonUtils.checkNull(groupLevel));
		    act.setOutData("list", cxandcxzid);
		    act.setOutData("cxzlist", cxzlist);
		    
		    act.setForword(this.STRATEGY_MODEL_SELECT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE,"三包策略页面物料组树初始");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 三包策略物料组树查询
	 * @see MaterialGroupTree.groupListQuery()
	 */
	@SuppressWarnings("unchecked")
	public void guaranteeGroupListQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//车型等级
			String strategyId = CommonUtils.checkNull(request.getParamValue("STRATEGYID"));	//三包策略ID
			String cxid = CommonUtils.checkNull(request.getParamValue("cxid")); //车系
			String cxzid =  CommonUtils.checkNull(request.getParamValue("cxzid")); //车型组
			String cxname = CommonUtils.checkNull(request.getParamValue("cxname"));//车型
			
			if(strategyId==null || "".equals(strategyId)) strategyId = "-1";
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			/*TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			PageResult<Map<String, Object>> ps=strategyDao.guaranteeGroupListQuery(groupId,groupCode,groupName,
					groupLevel,wrGroupId,curPage, 
					Constant.PAGE_SIZE,companyId,Long.parseLong(strategyId));*/
			
			 List<TmVhclMaterialGroupPO> list = dao.serviceActivityGroupName(); //车系及子车型组ID
				
				List cxandcxzid = new ArrayList();
				
				for(TmVhclMaterialGroupPO tpo : list){				
					Map map = new HashMap();
					List wrids = null;
					
					 wrids = dao.getXsCxzidsByCxid(tpo.getGroupId());	
					
					map.put("TmVhclMaterialGroupPO", tpo);
					map.put("CXZIDS", wrids);
					cxandcxzid.add(map);
				}
				List<TtAsWrModelGroupPO> cxzlist = dao.getCXZName(); //得到车型组集合
				
				PageResult<Map<String, Object>> ps = dao.getAllserviceActivityManageModelInfo2(cxid,cxzid,cxname,strategyId,curPage,Constant.PAGE_SIZE );	
				
			act.setOutData("ps", ps);
			act.setOutData("list", cxandcxzid);
		    act.setOutData("cxzlist", cxzlist);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(STRATEGY_MODEL_SELECT);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE,"三包策略页面物料组树查询结果");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存三包策略设定的车型
	 */
	public void saveGuaranteeStrategyModel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String modelId = request.getParamValue("modelId");//设置车型ID集合
			String strategyId = request.getParamValue("strategyId");//三包策略ID
			
			if(strategyId!=null && modelId!=null){
				//记录设定车型
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String today = sdf.format(new Date());
				TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
				strategyDao.saveGuaranteeStrategyModel(modelId, strategyId,
						logonUser.getUserId(), today);
			}
			act.setOutData("SUCESS", "SUCESS");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 删除选定三包策略设定车型
	 */
	public void deleteGuaranteeStrategyModel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String modelId[] = request.getParamValues("modelcb");//设置车型对应记录ID
			
			if(modelId!=null && modelId.length>0){
				String modelIds = "";
				for (int i = 0; i < modelId.length; i++) {
					modelIds = modelIds + modelId[i]+",";
				}
				modelIds = modelIds + "-1";
				TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
				strategyDao.deleteGuaranteeStrategyModel(modelIds);
			}
			act.setOutData("SUCESS", "SUCESS");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	 * 查询省份信息
	 */
	public void queryProvince(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			String strategyId = request.getParamValue("strategyId");
			
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			PageResult<Map<String,Object>> ps = strategyDao.queryProvince(Constant.REGION_TYPE_02,
					curPage, 200,strategyId);
			act.setOutData("ps", ps);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存三包策略设定省份信息
	 */
	public void saveGuaranteeStrategyProvince(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String strategyId = request.getParamValue("strategyId");
			String provinceId[] = request.getParamValues("provincecb");//设置车型对应记录ID
			
			if(strategyId!=null && provinceId!=null && provinceId.length>0){
				String provinceIds = "";
				for (int i = 0; i < provinceId.length; i++) {
					provinceIds = provinceIds + provinceId[i]+",";
				}
				provinceIds = provinceIds + "-1";
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String today = sdf.format(new Date());
				
				TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
				strategyDao.saveGuaranteeStrategyProvince(provinceIds,strategyId,
						logonUser.getUserId(),today);
			}
			act.setOutData("SUCESS", "SUCESS");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 删除三包策略设定省份信息
	 */
	public void deleteGuaranteeStrategyProvince(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String provinceId[] = request.getParamValues("provincecb");//设置车型对应记录ID
			
			if(provinceId!=null && provinceId.length>0){
				String provinceIs = "";
				for (int i = 0; i < provinceId.length; i++) {
					provinceIs = provinceIs + provinceId[i]+",";
				}
				provinceIs = provinceIs + "-1";
				TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
				strategyDao.deleteGuaranteeStrategyProvince(provinceIs);
			}
			act.setOutData("SUCESS", "SUCESS");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	 * 查询产地信息
	 */
	public void queryYieldly(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			String strategyId = request.getParamValue("strategyId");
			
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			PageResult<Map<String,Object>> ps = strategyDao.queryYieldly(curPage, 200,strategyId);
			act.setOutData("ps", ps);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询策略对应的身份信息
	 */
	public void queryStrategyYieldly(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
			PageResult<Map<String,Object>> ps = strategyDao.queryStrategyYieldly(Long.parseLong(id),
					curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", ps);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存三包策略设定产地信息
	 */
	public void saveGuaranteeStrategyYieldly(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String strategyId = request.getParamValue("strategyId");
			String yieldly[] = request.getParamValues("yieldlycb");//设置产地对应ID
			
			if(strategyId!=null && yieldly!=null && yieldly.length>0){
				String yieldlyIds = "";
				for (int i = 0; i < yieldly.length; i++) {
					yieldlyIds = yieldlyIds + yieldly[i]+",";
				}
				yieldlyIds = yieldlyIds + "-1";
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String today = sdf.format(new Date());
				
				TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
				strategyDao.saveGuaranteeStrategyYieldly(yieldlyIds,strategyId,
						logonUser.getUserId(),today);
			}
			act.setOutData("SUCESS", "SUCESS");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 删除三包策略设定产地信息
	 */
	public void deleteGuaranteeStrategyYieldly(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String yieldlyId[] = request.getParamValues("yieldlycb");//设置产地ID
			
			if(yieldlyId!=null && yieldlyId.length>0){
				String yieldlyIds = "";
				for (int i = 0; i < yieldlyId.length; i++) {
					yieldlyIds = yieldlyIds + yieldlyId[i]+",";
				}
				yieldlyIds = yieldlyIds + "-1";
				TreeGuaranteesStrategyDao strategyDao = TreeGuaranteesStrategyDao.getInstance();
				strategyDao.deleteGuaranteeStrategyYieldly(yieldlyIds);
			}
			act.setOutData("SUCESS", "SUCESS");
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,
					"三包策略维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void invoiceProbabilityMaintain(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(INVOICE_PROBABILITY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"开票概率维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	
	public void invoiceQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				String INVOICE_NAME = request.getParamValue("INVOICE_NAME");//项目代码
				String STATUS = request.getParamValue("STATUS");//项目名称
				if (Utility.testString(INVOICE_NAME)) {
					sb.append(" and upper(ip.INVOICE_NAME) like ? ");
					params.add("%"+INVOICE_NAME.toUpperCase()+"%");
				}
				if (Utility.testString(STATUS)) {
					sb.append(" and ip.status = ?");
					params.add(STATUS);
				}
				PageResult<Map<String, Object>> ps = dao.invoiceQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params,oemCompanyId);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void invoiceAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String idStr = request.getParamValue("ID"); // 状态
			TtInvoiceTaxratePO newTrPO = new TtInvoiceTaxratePO();
			TtInvoiceTaxratePO newTrPO1 = new TtInvoiceTaxratePO();
			if(idStr!=null){
				newTrPO.setId(Long.parseLong(idStr));
				List newTrPO1s= dao.select(newTrPO);
				if(newTrPO1s!=null && newTrPO1s.size()>0){
					newTrPO1=(TtInvoiceTaxratePO) newTrPO1s.get(0);
					act.setOutData("trPo", newTrPO1);
				}
			}
			act.setForword(INVOICE_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	
	/**
	 * 
	* @Title: claimOtherFeeUpdate 
	* @Description: TODO(索赔其它费用维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void invoiceFeeUpdate() {
		TtInvoiceTaxratePO newTrPO  = null;
		TtInvoiceTaxratePO oldTrPO = null;
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String invoiceName = request.getParamValue("INVOICE_NAME"); //费用ID
			String taxRate = request.getParamValue("TAX_RATE"); // 税率
			String status = request.getParamValue("STATUS"); // 状态
			String idStr = request.getParamValue("ID"); // 状态
			newTrPO = new TtInvoiceTaxratePO();
			newTrPO.setInvoiceName(invoiceName);
			newTrPO.setStatus(Long.parseLong(status));
			newTrPO.setTaxRate(Long.parseLong(taxRate));
			
			if(idStr==null||idStr==""){
				newTrPO.setId(Long.parseLong(SequenceManager.getSequence("")));
				dao.insert(newTrPO);
			}else{
				oldTrPO=new TtInvoiceTaxratePO();
				oldTrPO.setId(Long.parseLong(idStr));
				newTrPO.setId(Long.parseLong(idStr));
				dao.update(oldTrPO,newTrPO);
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔其它费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
