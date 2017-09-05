/**   
* @Title: FeeMain.java 
* @Package com.infodms.dms.actions.claim.basicData 
* @Description: TODO(保养费用维护Action) 
* @author wangjinbao   
* @date 2010-6-7 下午02:09:16 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.basicData;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.basicData.FeeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsWrModelFeePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: FeeMain 
 * @Description: TODO(保养费用维护Action) 
 * @author wangjinbao 
 * @date 2010-6-7 下午02:09:16 
 *  
 */
public class FeeMain {
	private Logger logger = Logger.getLogger(FeeMain.class);
	private final FeeDao dao = FeeDao.getInstance();
	private final String FEE_URL = "/jsp/claim/basicData/feeIndex.jsp";//主页面（查询）
	private final String FEE_UPDATE_URL = "/jsp/claim/basicData/feeModify.jsp";//增加（修改）页面
	/**
	 * 
	* @Title: feeInit 
	* @Description: TODO(保养费用维护初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void feeInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			List feeType = dao.getFeeType(Constant.FEE_STATUS.toString());//保养类型
			request.setAttribute("FEETYPE", feeType);//获得保养类型集合
			
			act.setForword(FEE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"保养费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: feeQuery 
	* @Description: TODO(保养费用维护查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void feeQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				String groupName = request.getParamValue("GROUP_NAME");//车型名称
				
				String groupCode = request.getParamValue("GROUP_CODE");//车型代码
				String flag = request.getParamValue("FLAG");//是否设置保养费用
				String flagStr = "";
				//拼sql的查询条件
				if (Utility.testString(groupName)) {//车型名称模糊查询
					sb.append(" and tvmg.group_name like ? ");
					params.add("%"+groupName+"%");
				}
				if (Utility.testString(groupCode)) {//车型代码模糊查询
					sb.append(" and upper(tvmg.group_code) like ? ");
					params.add("%"+groupCode.toUpperCase()+"%");
				}
				if(flag != null && "true".equals(flag)){//设置保养费用
					flagStr = " in ";
				}else{//未设置保养费用
					flagStr = " not in ";
				}				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.feeQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params,Constant.FEE_STATUS.toString(),flagStr);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"保养费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: feeUpdateInit 
	* @Description: TODO(保养费用维护修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void feeUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			List<Object> params = new LinkedList<Object>();
			StringBuffer sb = new StringBuffer();
			HashMap hm = null;
			String groupId = request.getParamValue("GROUP_ID");//车型id
			String flagStr = " in ";//默认设置保养费用
			List feeType = dao.getFeeType(Constant.FEE_STATUS.toString());//保养类型列表
			TmVhclMaterialGroupPO selpo = new TmVhclMaterialGroupPO();
			selpo.setGroupId(new Long(groupId));
			List list = dao.select(selpo);
			if(list != null && list.size() > 0){
				selpo = (TmVhclMaterialGroupPO)list.get(0);
			}
			if (Utility.testString(groupId)) {
				sb.append(" and tvmg.group_id = ? ");//拼sql的查询条件：车型id
				params.add(groupId);
			}
			PageResult<Map<String, Object>> rs = dao.feeQuery(Constant.PAGE_SIZE, 1,sb.toString(),params,Constant.FEE_STATUS.toString(),flagStr);
			List ls = rs.getRecords();
			if (ls!=null && ls.size()>0){//车型保养费用存在
					 hm = (HashMap)ls.get(0);
			}
			else{//不存在，构造
				hm = new HashMap();
				for(int i=0;i<feeType.size();i++){
					HashMap map = (HashMap)feeType.get(i);
					hm.put(map.get("CODE_ID"), "");
				}
			}
			request.setAttribute("FEE", hm);//保养参数对应的值
			request.setAttribute("GROUPPO", selpo);//车型PO
			request.setAttribute("FEETYPE", feeType);//查询动态列的集合
			
			act.setForword(FEE_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"保养费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: feeUpdate 
	* @Description: TODO(保养费用维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void feeUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			List feeType = dao.getFeeType(Constant.FEE_STATUS.toString());//保养类型列表
			String[] codenames = new String[feeType.size()];
			String[] codevalues = new String[feeType.size()];
			String[] codedescs = new String[feeType.size()];
			for(int i=0; i<feeType.size(); i++){
				HashMap tccode = (HashMap)feeType.get(i);
				codenames[i] = tccode.get("CODE_ID").toString();                          //获得保养类型对应的id
				codedescs[i] = tccode.get("CODE_DESC").toString();                        //获得保养类型对应的desc
				codevalues[i] = request.getParamValue(tccode.get("CODE_ID").toString());//获得保养类型对应的value
			}
			//车型id
			String groupId = request.getParamValue("groupId");//车型id
			TmVhclMaterialGroupPO selpo = new TmVhclMaterialGroupPO();
			selpo.setGroupId(new Long(groupId));
			List list = dao.select(selpo);
			if(list != null && list.size() > 0){
				selpo = (TmVhclMaterialGroupPO)list.get(0);
			}
			if (Utility.testString(groupId)) {
				TtAsWrModelFeePO seltmdp = null;
				TtAsWrModelFeePO retmdp = null;
				TtAsWrModelFeePO tmdp = null;
				TtAsWrModelFeePO tmaupdate = null;
				for(int i=0; i<codenames.length;i++){
					seltmdp = new TtAsWrModelFeePO();
					tmdp = new TtAsWrModelFeePO();
					tmaupdate = new TtAsWrModelFeePO();
					//根据车型id和保养类型的code查询保养费用表
					seltmdp.setModelId(Long.parseLong(groupId)); //车型id
					seltmdp.setFeeType(Integer.parseInt(codenames[i]));//临时类型
					retmdp = dao.getObjFromList(seltmdp);
					if(retmdp != null){//存在修改
						tmdp.setFeeId(retmdp.getFeeId());//保养类型的主键id
							
						tmaupdate.setFee(Utility.getDouble(codevalues[i]));//保养费用
						tmaupdate.setUpdateBy(logonUser.getUserId());
						tmaupdate.setUpdateDate(new Date());
							
						dao.updateFee(tmdp, tmaupdate);
					}else{//不存在新增
						tmaupdate.setFeeId(Long.parseLong(SequenceManager.getSequence("")));//保养费用ID
						tmaupdate.setCreateBy(logonUser.getUserId());
						tmaupdate.setCreateDate(new Date());
						tmaupdate.setModelId(Utility.getLong(groupId));   //车型ID
						tmaupdate.setFeeType(Utility.getInt(codenames[i]));//保养类型（tc_code）
						tmaupdate.setFee(Utility.getDouble(codevalues[i]));//保养费用
						tmaupdate.setIsSend(Constant.DOWNLOAD_CODE_STATUS_01);//下发状态：待下发
						dao.insert(tmaupdate);
					}
				}
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"保养费用维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: feeSendAll 
	* @Description: TODO(保养费用下发) 
	* @param    
	* @return void  
	* @throws
	 */
	public void feeSendAll(){
		
	}
	

}
