package com.infodms.dms.actions.claim.basicData;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jcifs.util.transport.Request;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.basicData.ClaimWrRuleDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TtAsWrMalfunctionPO;
import com.infodms.dms.po.TtAsWrMalfunctionPositionPO;
import com.infodms.dms.po.TtAsWrVinRulePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ClaimWrRule extends BaseImport{
	private Logger logger = Logger.getLogger(ClaimRule.class);
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private ClaimWrRuleDao dao = ClaimWrRuleDao.getInstance();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ClaimWrRuleInitUrl = "/jsp/claim/basicData/claimWrRulePreIndex.jsp";//查询页面
	private final String ClaimWrRuleAddInitUrl = "/jsp/claim/basicData/claimWrRuleAddPre.jsp";//新增初始化页面
	private final String ClaimWrRuleModifyUrl = "/jsp/claim/basicData/claimWrRuleModify.jsp";//修改页面
	
	
	/**
	 * 三包预警规则初始化查询
	 */
	public void claimWrRuleInit(){
		try {
			List<TcCodePO>list = dao.getPartWrType();
			act.setOutData("partWrTypeList", list);
			act.setForword(ClaimWrRuleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 三包预警规则查询
	 */
	public void claimWrRuleQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String vrCode = request.getParamValue("vrCode");//三包预警规则代码
			String vrType = request.getParamValue("vrType");//预警类型
			String vrLevel = request.getParamValue("vrLevel");//预警等级
			String partWrCode = request.getParamValue("partWrType");//配件类型
			TtAsWrVinRulePO rpo = new TtAsWrVinRulePO();
			rpo.setVrCode(vrCode);
			if(vrType != "" && vrType !=null){
				rpo.setVrType(Integer.parseInt(vrType));
			}
			if(!"".equals(vrLevel) && null!= vrLevel){
				rpo.setVrLevel(Integer.parseInt(vrLevel));
			}
			if(!"".equals(partWrCode) && null != partWrCode){
				rpo.setPartWrType(Integer.parseInt(partWrCode));
			}
			//设置页面
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getClaimWrRuleQuery(rpo,curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(ClaimWrRuleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 删除一条规则
	 */
	public void claimWrRuleDelete(){
		try {
			RequestWrapper request = act.getRequest();
			String vrId = request.getParamValue("vrId");
			TtAsWrVinRulePO rpo = new TtAsWrVinRulePO();
			rpo.setVrId(Long.parseLong(vrId));
			dao.delete(rpo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"三包预警规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 三包预警规则新增跳转
	 */
	public void claimWrRuleAddInit(){
		try {
			List<TmPtPartBasePO> partList = dao.getPartCode();//获取易损件
			List<TtAsWrMalfunctionPositionPO> posList = dao.getPosCode();//获取关注部位
			List<TcCodePO>list = dao.getPartWrType();//配件三包类型
			act.setOutData("partWrTypeList", list);
			act.setOutData("partList", partList);
			act.setOutData("posList", posList);
			act.setForword(ClaimWrRuleAddInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增三包预警规则
	 */
	public void claimWrRuleAdd(){
		try {
			RequestWrapper request = act.getRequest();
			String vrType = request.getParamValue("vrType");//预警类型
			String vrLevel = request.getParamValue("vrLevel");//预警等级
			String lawStandard = request.getParamValue("lawStandard");//法规
			String vrLaw1 = request.getParamValue("vrLaw1");//法定时间
			String vrWarranty1 = request.getParamValue("vrWarranty1");//预警时间
			String vrLaw2 = request.getParamValue("vrLaw2");//法定次数
			String vrWarranty2 = request.getParamValue("vrWarranty2");//预警次数
			
			TtAsWrVinRulePO rpo = new TtAsWrVinRulePO();
			rpo.setVrId(Long.parseLong(SequenceManager.getSequence("")));
			rpo.setVrCode("SBYJ"+rpo.getVrId());
			rpo.setVrLevel(Integer.parseInt(vrLevel));
			rpo.setCreateBy(logonUser.getUserId());
			rpo.setCreateDate(new Date());
			rpo.setVrLawStandard(lawStandard);
			
			if(Constant.VR_TYPE_1.equals(vrType)){
				rpo.setVrLaw(Integer.parseInt(vrLaw1));
				rpo.setVrWarranty(Integer.parseInt(vrWarranty1));
				rpo.setVrType(Integer.parseInt(Constant.VR_TYPE_1));
			}else if(Constant.VR_TYPE_2.equals(vrType)){
				String partWrType = request.getParamValue("partWrType");//配件类型
				rpo.setVrLaw(Integer.parseInt(vrLaw2));
				rpo.setVrWarranty(Integer.parseInt(vrWarranty2));
				rpo.setVrType(Integer.parseInt(Constant.VR_TYPE_2));
				rpo.setPartWrType(Integer.parseInt(partWrType));
				//配件代码和部位判断
				if(partWrType.equals(Constant.PART_WR_TYPE_2)){//易损件
					String part = request.getParamValue("part");//配件
					rpo.setVrPartCode(part);
				}else if(partWrType.equals(Constant.PART_WR_TYPE_3)){//部位
					String position = request.getParamValue("position");//关注部位
					rpo.setVrPartCode(position);
					if(vrLaw2.equals("1"))
					{
						rpo.setVrIsAtt(Constant.ALL_DAYS_1);
					}else
					{
						rpo.setVrIsAtt(Constant.ALL_DAYS_2);
					}
				}
			}
			dao.insert(rpo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 三包预警规则修改跳转
	 */
	public void claimWrRuleUpdateInit(){
		RequestWrapper req = act.getRequest();
		try {
			String id = req.getParamValue("id");
			TtAsWrVinRulePO po1 = new TtAsWrVinRulePO();
			po1.setVrId(Long.parseLong(id));
			List<TtAsWrVinRulePO> list = dao.select(po1);
			List<TmPtPartBasePO> partList = dao.getPartCode();//获取易损件
			List<TtAsWrMalfunctionPositionPO> posList = dao.getPosCode();//获取关注部位
			List<TcCodePO>typelist = dao.getPartWrType();//配件三包类型
			act.setOutData("partWrTypeList", typelist);
			act.setOutData("partList", partList);
			act.setOutData("posList", posList);
			act.setOutData("wrRuleList", list.get(0));
			act.setForword(ClaimWrRuleModifyUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 三包预警规则修改确认
	 */
	public void claimWrRuleUpdate(){
		RequestWrapper request = act.getRequest();
		String id = request.getParamValue("id");
		String vrType = request.getParamValue("vrType");//预警类型
		String vrLevel = request.getParamValue("vrLevel");//预警等级
		String lawStandard = request.getParamValue("lawStandard");//法规
		String vrLaw1 = request.getParamValue("vrLaw1");//法定时间
		String vrWarranty1 = request.getParamValue("vrWarranty1");//预警时间
		String vrLaw2 = request.getParamValue("vrLaw2");//法定次数
		String vrWarranty2 = request.getParamValue("vrWarranty2");//预警次数
		try {
			TtAsWrVinRulePO po1 = new TtAsWrVinRulePO();
			po1.setVrId(Long.parseLong(id));
			TtAsWrVinRulePO po2 = new TtAsWrVinRulePO();
			if(Constant.VR_TYPE_1.equals(vrType)){
				po2 = new TtAsWrVinRulePO();
				po2.setVrLevel(Integer.parseInt(vrLevel));
				po2.setVrLaw(Integer.parseInt(vrLaw1));
				po2.setVrWarranty(Integer.parseInt(vrWarranty1));
				po2.setVrLawStandard(lawStandard);
				po2.setUpdateBy(logonUser.getUserId());
				po2.setUpdateDate(new Date());
			}else if(Constant.VR_TYPE_2.equals(vrType)){
				String partWrType = request.getParamValue("partWrType");
				po2 = new TtAsWrVinRulePO();
				po2.setVrLevel(Integer.parseInt(vrLevel));
				po2.setVrLaw(Integer.parseInt(vrLaw2));
				po2.setVrWarranty(Integer.parseInt(vrWarranty2));
				po2.setVrLawStandard(lawStandard);
				po2.setUpdateBy(logonUser.getUserId());
				po2.setUpdateDate(new Date());
				if(Constant.PART_WR_TYPE_2.equals(partWrType)){
					String part = request.getParamValue("part");
					po2.setVrPartCode(part);
				}else if(Constant.PART_WR_TYPE_3.equals(partWrType)){
					String pos = request.getParamValue("position");
					po2.setVrPartCode(pos);
					if(vrLaw2.equals("1"))
					{
						po2.setVrIsAtt(Constant.ALL_DAYS_1);
					}else
					{
						po2.setVrIsAtt(Constant.ALL_DAYS_2);
					}
				}
			}
			dao.update(po1, po2);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"三包预警规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 检验三包预警规则是否存在
	 * 配件类型为常规件：根据配件类型，法定时间，预警时间,预警等级判断
	 * 配件类型为易损件：根据配件代码，法定时间，预警时间判断
	 * 配件类型为关注部位：根据部位代码，法定时间，预警时间判断
	 */
	public void checkUnique(){
		RequestWrapper request = act.getRequest();
		try {
			String type = request.getParamValue("vrType");//预警类型
			String partWrType = request.getParamValue("partWrType");//配件类型
			String vrLaw1 = request.getParamValue("vrLaw1");//法定时间
			String vrWarranty1 = request.getParamValue("vrWarranty1");//预警时间
			String vrLaw2 = request.getParamValue("vrLaw2");//法定次数
			String vrWarranty2 = request.getParamValue("vrWarranty2");//预警次数
			String part = request.getParamValue("part");//配件
			String position = request.getParamValue("position");//关注部位
			String level = request.getParamValue("vrLevel");//预警等级
			String sta = request.getParamValue("sta");//1,新增；2，修改
			String id = CommonUtils.checkNull(request.getParamValue("id"));//VR_ID
			List<TtAsWrVinRulePO> list = new LinkedList<TtAsWrVinRulePO>();
			//判断新增规则是否已经存在
			if(type.equals(Constant.VR_TYPE_1)){
				list = dao.checkRuleExist(sta,id,type,partWrType,vrLaw1,vrWarranty1,part,position,level);
			}else if(type.equals(Constant.VR_TYPE_2)){
				list = dao.checkRuleExist(sta,id,type,partWrType,vrLaw2,vrWarranty2,part,position,level);
			}
			
			if(list!=null && list.size()>0){//已经存在
				act.setOutData("success", "false");
			}else{
				//判断各等级的预警时间或次数：一级〉二级〉三级...
				List<TtAsWrVinRulePO> smallPo = dao.getClaimWrRuleByPart(type,level,partWrType,part,position,"small");//
				List<TtAsWrVinRulePO> bigPo = dao.getClaimWrRuleByPart(type,level,partWrType,part,position,"big");
				
				if((smallPo!=null&&smallPo.size()>0) && (bigPo!=null&&bigPo.size()>0)){
					if(type.equals(Constant.VR_TYPE_1)){
						if((smallPo.get(0).getVrWarranty() > Integer.parseInt(vrWarranty1)) && bigPo.get(0).getVrWarranty() < Integer.parseInt(vrWarranty1)){
							act.setOutData("success", "true");
						}
					}else if(type.equals(Constant.VR_TYPE_2)){
						if((smallPo.get(0).getVrWarranty() > Integer.parseInt(vrWarranty2)) && bigPo.get(0).getVrWarranty() < Integer.parseInt(vrWarranty2)){
							act.setOutData("success", "true");
						}
					}
				}else{
					if((smallPo == null||smallPo.size()==0) && (bigPo != null&&bigPo.size()>0)){
						if(type.equals(Constant.VR_TYPE_1)){
							if(bigPo.get(0).getVrWarranty() < Integer.parseInt(vrWarranty1)){
								act.setOutData("success", "true");
							}
						}else if(type.equals(Constant.VR_TYPE_2)){
							if(bigPo.get(0).getVrWarranty() < Integer.parseInt(vrWarranty2)){
								act.setOutData("success", "true");
							}
						}
					}
					if((smallPo != null&&smallPo.size()>0) && (bigPo == null||bigPo.size()==0)){
						if(type.equals(Constant.VR_TYPE_1)){
							if(smallPo.get(0).getVrWarranty() > Integer.parseInt(vrWarranty1)){
								act.setOutData("success", "true");
							}
						}else if(type.equals(Constant.VR_TYPE_2)){
							if(smallPo.get(0).getVrWarranty() > Integer.parseInt(vrWarranty2)){
								act.setOutData("success", "true");
							}
						}
					}
					if((smallPo == null||smallPo.size()==0) && (bigPo == null||bigPo.size()==0)){
						act.setOutData("success", "true");
					}
				}
				
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
