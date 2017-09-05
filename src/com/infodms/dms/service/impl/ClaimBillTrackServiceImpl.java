package com.infodms.dms.service.impl;

import java.util.Date;
import java.util.List;

import com.infodms.dms.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.po.TtAsWrApplicationCounterPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrCompensationAppPO;
import com.infodms.dms.po.TtAsWrCompensationCounterPO;
import com.infodms.dms.po.TtAsWrLabouritemCounterPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrOutrepairCounterPO;
import com.infodms.dms.po.TtAsWrOutrepairMyCounterPO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infodms.dms.po.TtAsWrPartsitemCounterPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtClaimAccessoryCounterPO;
import com.infodms.dms.po.TtClaimAccessoryDtlPO;
import com.infodms.dms.service.ClaimBillTrackService;
import com.infodms.yxdms.entity.claim.TtAsWrOutrepairMoneyPO;
import com.infoservice.mvc.context.ActionContext;

public class ClaimBillTrackServiceImpl implements ClaimBillTrackService {

	private final ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
	

	public List<TtAsWrApplicationCounterPO> queryClaimCounterByCounterObj(
			TtAsWrApplicationCounterPO obj) {
		List<TtAsWrApplicationCounterPO> objList = dao.select(obj);
		return objList;
	}

	public List<TtAsWrApplicationCounterPO> queryClaimCounterByObj(
			TtAsWrApplicationPO obj) {
		TtAsWrApplicationCounterPO cpo = new TtAsWrApplicationCounterPO();
		BeanUtils.copyProperties(obj, cpo);
		return queryClaimCounterByCounterObj(cpo);
	}

	public boolean claimCounter(TtAsWrApplicationCounterPO obj,
			String counterRemark) throws Exception {
		boolean flag = false;
		List<TtAsWrApplicationCounterPO> existList = queryClaimCounterByCounterObj(obj);
		// 校验是否已反索赔,只有未反索赔的索赔单才能够反索赔
		if(!CommonUtils.isNullList(existList)){
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Date now = new Date();
			
			// 写入索赔单信息
			TtAsWrApplicationPO poCondition = new TtAsWrApplicationPO();
			BeanUtils.copyProperties(obj, poCondition);
			List<TtAsWrApplicationPO> apoList = dao.select(poCondition);
			if(!CommonUtils.isNullList(apoList)){
				throw new RuntimeException("未查到指定的索赔单信息！");
			} else if(apoList.size()>1){
				throw new RuntimeException("数据异常，查出多条索赔单信息！");
			}
			poCondition = apoList.get(0);
			TtAsWrApplicationCounterPO counterPO = new TtAsWrApplicationCounterPO();
			BeanUtils.copyProperties(poCondition, counterPO);
			counterPO.setCounterRemark(counterRemark);
			counterPO.setCreateDate(now);
			counterPO.setCreateBy(logonUser.getUserId());
			counterPO.setUpdateDate(counterPO.getCreateDate());
			counterPO.setUpdateBy(counterPO.getCreateBy());
			counterPO.setBalanceNo(null);
			counterPO.setReportDate(now);
			counterPO.setIsInvoice(0);
			counterPO.setInvoiceDate(null);
			dao.insert(counterPO);
			
			String tempId = "";
			
			// 写入配件信息
			TtAsWrPartsitemPO partCondition = new TtAsWrPartsitemPO();
			partCondition.setId(poCondition.getId());
			List<TtAsWrPartsitemPO> partList = dao.select(partCondition);
			if(!CommonUtils.isNullList(partList)){
				for (TtAsWrPartsitemPO part : partList) {
					// 校验是否是同一个单的记录,以防条件异常,查询出了所有的数据
					if(StringUtils.isNotEmpty(tempId)&&!tempId.equals(part.getId())){
						throw new RuntimeException("数据异常，查出其他索赔单的配件信息！请联系管理员查看！"+tempId+":"+part.getId());
					}
					TtAsWrPartsitemCounterPO cpo = new TtAsWrPartsitemCounterPO();
					BeanUtils.copyProperties(part, cpo);
					dao.insert(cpo);
					tempId = String.valueOf(part.getId());
				}
			}
			
			tempId = "";
			// 写入工时信息
			TtAsWrLabouritemPO labourCondition = new TtAsWrLabouritemPO();
			labourCondition.setId(poCondition.getId());
			List<TtAsWrLabouritemPO> labourList = dao.select(labourCondition);
			if(!CommonUtils.isNullList(labourList)){
				for (TtAsWrLabouritemPO labour : labourList) {
					// 校验是否是同一个单的记录,以防条件异常,查询出了所有的数据
					if(StringUtils.isNotEmpty(tempId)&&!tempId.equals(labour.getId())){
						throw new RuntimeException("数据异常，查出其他索赔单的工时信息！请联系管理员查看！"+tempId+":"+labour.getId());
					}
					TtAsWrLabouritemCounterPO cpo = new TtAsWrLabouritemCounterPO();
					BeanUtils.copyProperties(labour, cpo);
					dao.insert(cpo);
					tempId = String.valueOf(labour.getId());
				}
			}
			
			tempId = "";
			
			// 写入外出信息
			TtAsWrOutrepairPO outCondition = new TtAsWrOutrepairPO();
			outCondition.setId(poCondition.getId());
			List<TtAsWrOutrepairPO> outList = dao.select(outCondition);
			if(!CommonUtils.isNullList(outList)){
				for (TtAsWrOutrepairPO outPO : outList) {
					// 校验是否是同一个单的记录,以防条件异常,查询出了所有的数据
					if(StringUtils.isNotEmpty(tempId)&&!tempId.equals(outPO.getId())){
						throw new RuntimeException("数据异常，查出其他索赔单的外出信息！请联系管理员查看！"+tempId+":"+outPO.getId());
					}
					TtAsWrOutrepairCounterPO cpo = new TtAsWrOutrepairCounterPO();
					BeanUtils.copyProperties(outPO, cpo);
					dao.insert(cpo);
					tempId = String.valueOf(outPO.getId());
				}
			}
			
			tempId = "";

			// 写入外出金额信息
			TtAsWrOutrepairMoneyPO outMoneyCondition = new TtAsWrOutrepairMoneyPO();
			outMoneyCondition.setOutId(poCondition.getId());
			List<TtAsWrOutrepairMoneyPO> outMoneyList = dao.select(outMoneyCondition);
			if(!CommonUtils.isNullList(outMoneyList)){
				for (TtAsWrOutrepairMoneyPO outPO : outMoneyList) {
					// 校验是否是同一个单的记录,以防条件异常,查询出了所有的数据
					if(StringUtils.isNotEmpty(tempId)&&!tempId.equals(outPO.getOutId())){
						throw new RuntimeException("数据异常，查出其他索赔单的外出金额信息！请联系管理员查看！"+tempId+":"+outPO.getOutId());
					}
					TtAsWrOutrepairMyCounterPO cpo = new TtAsWrOutrepairMyCounterPO();
					BeanUtils.copyProperties(outPO, cpo);
					dao.insert(cpo);
					tempId = String.valueOf(outPO.getOutId());
				}
			}
			
			tempId = "";

			// 写入补偿费明细
			TtAsWrCompensationAppPO compCondition = new TtAsWrCompensationAppPO();
			compCondition.setClaimNo(poCondition.getClaimNo());
			List<TtAsWrCompensationAppPO> compList = dao.select(compCondition);
			if(!CommonUtils.isNullList(compList)){
				for (TtAsWrCompensationAppPO compPO : compList) {
					// 校验是否是同一个单的记录,以防条件异常,查询出了所有的数据
					if(StringUtils.isNotEmpty(tempId)&&!tempId.equals(compPO.getClaimNo())){
						throw new RuntimeException("数据异常，查出其他索赔单的补偿费信息！请联系管理员查看！"+tempId+":"+compPO.getClaimNo());
					}
					TtAsWrCompensationCounterPO cpo = new TtAsWrCompensationCounterPO();
					BeanUtils.copyProperties(compPO, cpo);
					dao.insert(cpo);
					tempId = String.valueOf(compPO.getClaimNo());
				}
			}

			tempId = "";

			// 写入辅料明细
			TtClaimAccessoryDtlPO accDtlCondition = new TtClaimAccessoryDtlPO();
			accDtlCondition.setClaimNo(poCondition.getClaimNo());
			List<TtClaimAccessoryDtlPO> accDtlList = dao.select(accDtlCondition);
			if(!CommonUtils.isNullList(accDtlList)){
				for (TtClaimAccessoryDtlPO accDtl : accDtlList) {
					// 校验是否是同一个单的记录,以防条件异常,查询出了所有的数据
					if(StringUtils.isNotEmpty(tempId)&&!tempId.equals(accDtl.getClaimNo())){
						throw new RuntimeException("数据异常，查出其他索赔单的辅料费信息！请联系管理员查看！"+tempId+":"+accDtl.getClaimNo());
					}
					TtClaimAccessoryCounterPO cpo = new TtClaimAccessoryCounterPO();
					BeanUtils.copyProperties(accDtl, cpo);
					dao.insert(cpo);
					tempId = String.valueOf(accDtl.getClaimNo());
				}
			}
			
			flag = true;
		} else {
			if(existList.size()>1){
				throw new RuntimeException("通过索赔单查出了多个反索赔信息，请联系系统管理员查询原因！");
			} else if(existList.size()==1){
				throw new RuntimeException("该索赔单已经反索赔，不能再次反索赔！");
			}
		}
		return flag;
	}

}
