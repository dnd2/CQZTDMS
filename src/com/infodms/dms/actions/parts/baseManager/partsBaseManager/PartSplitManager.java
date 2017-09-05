package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartSplitDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartSplitDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PartSplitManager 
 * @Description   : 配件拆合比例设置 
 * @author        : chenjunjiang
 * CreateDate     : 2013-4-10
 */
public class PartSplitManager implements PTConstants{

	public Logger logger = Logger.getLogger(PartSplitManager.class);
	private PartSplitDao dao = PartSplitDao.getInstance();
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件拆合比例设置查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public void partSplitQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(PART_SPLIT_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件拆合比例设置");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询配件拆合比例 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public void queryPartSplitInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String partOldCode = CommonUtils.checkNull(request
					.getParamValue("PART_OLDCODE"));// 总成件配件编码
			partOldCode = partOldCode.toUpperCase();
			String SubPartOldCode = CommonUtils.checkNull(request
					.getParamValue("SUBPART_OLDCODE"));// 分总成件编码
			SubPartOldCode = SubPartOldCode.toUpperCase();
			String str_state = CommonUtils.checkNull(request
					.getParamValue("STATE"));// 是否有效
			
			TtPartSplitDefinePO bean = new TtPartSplitDefinePO();
			bean.setPartOldcode(partOldCode);
			bean.setSubpartOldcode(SubPartOldCode);
			if(!"".equals(str_state)){
				bean.setState(CommonUtils.parseInteger(str_state));
			}
			
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartSplitList(bean, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件拆分设置");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 新增初始化,跳转到新增页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public void addPartSplitInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(PART_SPLIT_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ADD_FAILURE_CODE, "配件拆合比例添加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 新增 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public void addPartSplitInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			int index = 0;
           String str_index = request.getParamValue("myIndex");//分件序号
           String str_partId = request.getParamValue("PART_ID");//总成件ID
           String partCode = request.getParamValue("PART_CODE");//总成件件号
           String partOldCode = request.getParamValue("PART_OLDCODE");//总成件配件编码
           String partCname = request.getParamValue("PART_CNAME");//总成件配件名称
           
           Map<String, Object> map =  dao.checkPartId(str_partId);//检测总成件,如果分件设置表中已经存在就提示错误信息
           if (map != null) {// 已经存在
				act.setOutData("error", "该总成件已经存在拆分关系,请重新选择或修改!");
				return;
			}
           
           if(!"".equals(CommonUtils.checkNull(str_index))){
        	   index = CommonUtils.parseInteger(str_index);
           }
		   for(int i = 1;i<index;i++){//获取分件信息
			  String str_subPartId = request.getParamValue("SUBPART_ID"+i);
			  String subPartCode = request.getParamValue("SUBPART_CODE"+i);
			  String subPartOldCode = request.getParamValue("SUBPART_OLDCODE"+i);
			  String subPartCname = request.getParamValue("SUBPART_CNAME"+i);
			  String str_splitNum =  request.getParamValue("SPLIT_NUM"+i);
			  //String str_costRate = request.getParamValue("COST_RATE"+i);
			  String remark = request.getParamValue("REMARK"+i);
			  
			   TtPartSplitDefinePO ttPartSplitDefinePO = new TtPartSplitDefinePO();
			   ttPartSplitDefinePO.setSplitId(CommonUtils.parseLong(SequenceManager.getSequence("")));
			   ttPartSplitDefinePO.setPartId(CommonUtils.parseLong(str_partId));
			   ttPartSplitDefinePO.setPartCode(partCode);
			   ttPartSplitDefinePO.setPartOldcode(partOldCode);
			   ttPartSplitDefinePO.setPartCname(partCname);
			   ttPartSplitDefinePO.setSubpartId(CommonUtils.parseLong(str_subPartId));
			   ttPartSplitDefinePO.setSubpartCode(subPartCode);
			   ttPartSplitDefinePO.setSubpartOldcode(subPartOldCode);
			   ttPartSplitDefinePO.setSubpartCname(subPartCname);
			   ttPartSplitDefinePO.setSplitNum(CommonUtils.parseLong(str_splitNum));
			   //ttPartSplitDefinePO.setCostRate(CommonUtils.parseFloat(str_costRate));
			   ttPartSplitDefinePO.setCostRate(0f);
			   ttPartSplitDefinePO.setRemark(remark);
			   ttPartSplitDefinePO.setCreateDate(new Date());
			   ttPartSplitDefinePO.setCreateBy(logonUser.getUserId());
			   
			   dao.insert(ttPartSplitDefinePO);
			}

		   act.setOutData("success", "添加成功!");
		} catch (Exception e) {
			act.setOutData("error", "填写的拆分成本比例数据无效!");
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ADD_FAILURE_CODE, "配件拆合比例");
			logger.error(logonUser, e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查看配件拆分信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public void viewPartSplitInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId")); //总成件Id
			TtPartSplitDefinePO ttPartSplitDefinePO = new TtPartSplitDefinePO();
			ttPartSplitDefinePO.setPartId(CommonUtils.parseLong(partId));
			List splitInfo = dao.select(ttPartSplitDefinePO);
			ttPartSplitDefinePO.setStatus(1);
			TtPartSplitDefinePO partSplit =  (TtPartSplitDefinePO) (splitInfo.get(1));//获取总件信息
			act.setOutData("partSplit", partSplit);
			act.setOutData("splitInfo", splitInfo);
		    
			act.setForword(PART_SPLIT_INFO_VIEW);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件拆合比例信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 失效 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void celPartSplit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			String partId = CommonUtils.checkNull(request
					.getParamValue("partId")); // 总成件Id
			String curPage = CommonUtils.checkNull(request
					.getParamValue("curPage"));// 当前页
    
			TtPartSplitDefinePO spo = new TtPartSplitDefinePO();// 源po
			
			spo.setPartId(CommonUtils.parseLong(partId));

			TtPartSplitDefinePO po = new TtPartSplitDefinePO();// 更新po
			po.setState(Constant.STATUS_DISABLE);
			po.setDisableBy((logonUser.getUserId()));
			po.setDisableDate(new Date());

			if ("".equals(curPage)) {
				curPage = "1";
			}
			dao.update(spo, po);
			act.setOutData("success", "失效成功!");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件拆分失效");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 设置分件有效 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void validPartSplit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			String partId = CommonUtils.checkNull(request
					.getParamValue("partId")); // 总成件Id
			String curPage = CommonUtils.checkNull(request
					.getParamValue("curPage"));// 当前页
    
			TtPartSplitDefinePO spo = new TtPartSplitDefinePO();// 源po
			
			spo.setPartId(CommonUtils.parseLong(partId));

			TtPartSplitDefinePO po = new TtPartSplitDefinePO();// 更新po
			po.setState(Constant.STATUS_ENABLE);
			if ("".equals(curPage)) {
				curPage = "1";
			}
			dao.update(spo, po);
			act.setOutData("success", "设置有效成功!");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件分件有效");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 获取当前总成件信息,并转到修改页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void queryPartSplitDetail(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId")); //总成件Id
			TtPartSplitDefinePO ttPartSplitDefinePO = new TtPartSplitDefinePO();
			ttPartSplitDefinePO.setPartId(CommonUtils.parseLong(partId));
			ttPartSplitDefinePO.setStatus(1);
			List splitInfo = dao.select(ttPartSplitDefinePO);
			TtPartSplitDefinePO partSplit = new TtPartSplitDefinePO();
			if(splitInfo.size()>0){
				partSplit =  (TtPartSplitDefinePO) (splitInfo.get(0));//获取总件信息
			}
			act.setOutData("partSplit", partSplit);
			act.setOutData("splitInfo", splitInfo);
		    
			act.setForword(PART_SPLIT_INFO_MOD);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件拆合比例信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除分件 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void deletePartSplit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);

		try {
			String splitId = CommonUtils.checkNull(request
					.getParamValue("splitId")); // 配件拆分Id
			String partId = CommonUtils.checkNull(request
					.getParamValue("partId")); // 配件Id

			TtPartSplitDefinePO spo = new TtPartSplitDefinePO();// 源po
			
			spo.setSplitId(CommonUtils.parseLong(splitId));

			TtPartSplitDefinePO po = new TtPartSplitDefinePO();// 更新po
			po.setStatus(0);
			dao.update(spo, po);
			act.setOutData("success", "删除成功!");
			act.setOutData("partId", partId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件分件有效");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改拆分信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void updatePartSplitInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
		   RequestWrapper request = act.getRequest();
		   int index = 0;
           String str_index = request.getParamValue("myIndex");//分件序号
           String str_partId = request.getParamValue("PART_ID");//总成件ID
           String partCode = request.getParamValue("PART_CODE");//总成件件号
           String partOldCode = request.getParamValue("PART_OLDCODE");//总成件配件编码
           String partCname = request.getParamValue("PART_CNAME");//总成件配件名称
           String[] splitIds = request.getParamValues("SPLIT_ID");//拆分记录id
           
           for(int i=1;i<=splitIds.length;i++){//修改已经存在分件信息
        	  String str_subPartId = request.getParamValue("SUBPART_ID"+i);//分件id
 			  String subPartCode = request.getParamValue("SUBPART_CODE"+i);//分件件号
 			  String subPartOldCode = request.getParamValue("SUBPART_OLDCODE"+i);//分件编码
 			  String subPartCname = request.getParamValue("SUBPART_CNAME"+i);//分件名称
        	  String str_splitNum =  request.getParamValue("SPLIT_NUM"+i);//拆分数量
        	  String str_costRate = request.getParamValue("COST_RATE"+i);//拆分比列
 			  String remark = request.getParamValue("REMARK"+i);//备注
        	   
        	  TtPartSplitDefinePO spo = new TtPartSplitDefinePO();//源po
			  spo.setSplitId(CommonUtils.parseLong(splitIds[i-1]));
			  
			  TtPartSplitDefinePO po = new TtPartSplitDefinePO();//更新po
			  po.setSubpartId(CommonUtils.parseLong(str_subPartId));
			  po.setSubpartCode(subPartCode);
			  po.setSubpartOldcode(subPartOldCode);
			  po.setSubpartCname(subPartCname);
			  po.setSplitNum(CommonUtils.parseLong(str_splitNum));
			  po.setCostRate(CommonUtils.parseFloat(str_costRate));
			  po.setRemark(remark);
			  
			  dao.update(spo, po);
           }
           
           if(!"".equals(CommonUtils.checkNull(str_index))){
        	   index = CommonUtils.parseInteger(str_index);
           }
           if((index-1)>splitIds.length){//如果有新添加的分件
        	   for(int i = index-(index-(splitIds.length+1));i<index;i++){//新增添加的分件信息
     			  String str_subPartId = request.getParamValue("SUBPART_ID"+i);
     			  String subPartCode = request.getParamValue("SUBPART_CODE"+i);
     			  String subPartOldCode = request.getParamValue("SUBPART_OLDCODE"+i);
     			  String subPartCname = request.getParamValue("SUBPART_CNAME"+i);
     			  String str_splitNum =  request.getParamValue("SPLIT_NUM"+i);
     			  String str_costRate = request.getParamValue("COST_RATE"+i);
     			  String remark = request.getParamValue("REMARK"+i);
     			   TtPartSplitDefinePO ttPartSplitDefinePO = new TtPartSplitDefinePO();
     			   ttPartSplitDefinePO.setSplitId(CommonUtils.parseLong(SequenceManager.getSequence("")));
     			   ttPartSplitDefinePO.setPartId(CommonUtils.parseLong(str_partId));
     			   ttPartSplitDefinePO.setPartCode(partCode);
     			   ttPartSplitDefinePO.setPartOldcode(partOldCode);
     			   ttPartSplitDefinePO.setPartCname(partCname);
     			   ttPartSplitDefinePO.setSubpartId(CommonUtils.parseLong(str_subPartId));
     			   ttPartSplitDefinePO.setSubpartCode(subPartCode);
     			   ttPartSplitDefinePO.setSubpartOldcode(subPartOldCode);
     			   ttPartSplitDefinePO.setSubpartCname(subPartCname);
     			   ttPartSplitDefinePO.setSplitNum(CommonUtils.parseLong(str_splitNum));
     			   ttPartSplitDefinePO.setCostRate(CommonUtils.parseFloat(str_costRate));
     			   ttPartSplitDefinePO.setRemark(remark);
     			   ttPartSplitDefinePO.setCreateDate(new Date());
     			   ttPartSplitDefinePO.setCreateBy(logonUser.getUserId());
     			   
     			   dao.insert(ttPartSplitDefinePO);
     			  
     			}
           }

		   act.setOutData("success", "修改成功!");
		} catch (Exception e) {
			act.setOutData("error", "配件拆合比例修改失败!");
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件拆合比例");
			logger.error(logonUser, e1);
			
		}
	}
}
