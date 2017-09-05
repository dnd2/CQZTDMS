package com.infodms.yxdms.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 公共的action类
 * @author yuewei
 *
 */
public class BaseAction {
	
	protected static Logger logger = Logger.getLogger(BaseAction.class);
	protected ActionContext act = ActionContext.getContext();
	protected AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	protected RequestWrapper request = act.getRequest();
	protected ResponseWrapper response = act.getResponse();
	
	/**
	 * 设置当月的开始时间 -结束时间 例如2014-10-01 2014-10-31
	 * @param startTime
	 * @param endTime
	 */
	public void setCurrMonthTime(String startTime,String endTime){
		RemindDateUtils dateUtils = new RemindDateUtils();
		Date StartTime = dateUtils.getCurrentMonthStartTime();
		Date EndTime = dateUtils.getCurrentMonthEndTime();
		act.setOutData(startTime, BaseUtils.printDate("yyyy-MM-dd", StartTime));
		act.setOutData(endTime, BaseUtils.printDate("yyyy-MM-dd", EndTime));
	}
	/**
	 * 取主键ID
	 * @return
	 */
	protected static Long getSeq(){
		return BaseUtils.ConvertLong(SequenceManager.getSequence(""));
	} 
	
	/**
	 * 返回request得到的参数并检验为空就转化为""
	 * @return
	 */
	protected String getParam(String str) {
		return BaseUtils.checkNull(request.getParamValue(str));
	}
	/**
	 * 得到当前的DealerId
	 * @return
	 */
	protected Long getCurrDealerId() {
		return BaseUtils.ConvertLong(loginUser.getDealerId());
	}
	/**
	 * 分页方法 begin
	 * @return
	 */
	protected Integer getCurrPage() {
		return request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页	
	}
	/**
	 * 公共的带数据信息的跳转
	 * @param dataParm 数据命名
	 * @param data 传送数据
	 * @param url 跳转路径
	 * @param msg 错误消息
	 */
	protected void sendMsgByUrl(String dataParm,PageResult<Map<String,Object>> data,String url, String msg) {
		try {
			act.setForword(url);
			act.setOutData(dataParm==null?"ps":dataParm,data);
		} catch (Exception e) {
			BizException bizexception = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, msg);
			logger.error(logger, bizexception);
			act.setException(bizexception);
		}
	}
	/**
	 * 公共的的跳转
	 * @param url 跳转路径
	 * @param msg 错误消息
	 */
	protected void sendMsgByUrl(String url, String msg) {
		try {
			act.setForword(url);
		} catch (Exception e) {
			BizException bizexception = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, msg);
			logger.error(logger, bizexception);
			act.setException(bizexception);
		}
	}
	/**
	 * 公共的的跳转
	 * @param catalogue jsp包名
	 * @param jspname jsp名称
	 * @param msg 信息
	 */
	protected void sendMsgByUrl(String catalogue,String jspname,String msg) {
		try {
			String url="/jsp_new"+"/"+catalogue+"/"+jspname+".jsp";
			act.setForword(url);
		} catch (Exception e) {
			BizException bizexception = new BizException(act, e,
					ErrorCodeConstant.ACTION_NAME_ERROR_CODE, msg);
			logger.error(logger, bizexception);
			act.setException(bizexception);
		}
	}
	/**
	 * json 数据返回值设置
	 * succ 1 为成功
	 * succ -1 为失败
	 * @param res
	 */
	public void setJsonSuccByres(int res){
		if(res==1){
			act.setOutData("succ", 1);
		}else{
			act.setOutData("succ", -1);
		}
	}
	/**
	 * json 数据返回值设置
	 * flag true 
	 * flag false 为失败
	 * @param res
	 */
	public void setJsonSuccByres(boolean res){
		if(res==true){
			act.setOutData("flag", true);
		}else{
			act.setOutData("flag", false);
		}
	}
	protected Double getListCount(List<Map<String, Object>> list,String name) {
		Double tatol=0.0d;
		for(int i=0;i<list.size();i++){
			tatol+=Double.valueOf(list.get(i).get(name).toString());
		}
		return tatol;
	}
	protected Double getListCount(Double tatol,List<Map<String, Object>> list,String name) {
		for(int i=0;i<list.size();i++){
			tatol+=Double.valueOf(list.get(i).get(name).toString());
		}
		return tatol;
	}
	/**
	 * 检测List<Map<?>是否为空
	 * @param list
	 * @return
	 */
	public boolean checkListNull(List<?> list){
		boolean flag=false;
		if(list!=null&&list.size()>0){
			flag=true;
		}
		return flag;
	}
}
