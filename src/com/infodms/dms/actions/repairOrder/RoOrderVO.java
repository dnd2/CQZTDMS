package com.infodms.dms.actions.repairOrder;

import java.util.List;

import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;

/**
 * 预授权申请单详细信息
 * @author KFQ
 */
public class RoOrderVO {
	/**工单信息*/
	private TtAsRepairOrderPO repairPo;
	/** 预授权请单信息 */
	private TtAsWrForeapprovalPO forePo;
	/** 车辆信息 */
	private TmVehiclePO vehiclePO;
	/** 需要预授权对应配件信息(全部) */
	private List<TtAsRoRepairPartPO> partsList;
	/** 经销商信息 */
	private TmDealerPO dealerPO;
	/** 维修工时总费用*/
	private Double labourAmount;
	/** 维修累计天数*/
	private Integer repairDays;
	/** 配件三包期内总金额*/
	private Double warAmount;
	
	/** 维修的总费用*/
	private Double repairTotal;
	
	public TtAsRepairOrderPO getRepairPo() {
		return repairPo;
	}

	public void setRepairPo(TtAsRepairOrderPO repairPo) {
		this.repairPo = repairPo;
	}

	public Double getRepairTotal() {
		return repairTotal;
	}

	public void setRepairTotal(Double repairTotal) {
		this.repairTotal = repairTotal;
	}

	public TmVehiclePO getVehiclePO() {
		return vehiclePO;
	}

	public void setVehiclePO(TmVehiclePO vehiclePO) {
		this.vehiclePO = vehiclePO;
	}

	public List<TtAsRoRepairPartPO> getPartsList() {
		return partsList;
	}

	public void setPartsList(List<TtAsRoRepairPartPO> partsList) {
		this.partsList = partsList;
	}

	public TmDealerPO getDealerPO() {
		return dealerPO;
	}

	public void setDealerPO(TmDealerPO dealerPO) {
		this.dealerPO = dealerPO;
	}

	public TtAsWrForeapprovalPO getForePo() {
		return forePo;
	}

	public void setForePo(TtAsWrForeapprovalPO forePo) {
		this.forePo = forePo;
	}

	public Double getLabourAmount() {
		return labourAmount;
	}

	public void setLabourAmount(Double labourAmount) {
		this.labourAmount = labourAmount;
	}

	public Integer getRepairDays() {
		return repairDays;
	}

	public void setRepairDays(Integer repairDays) {
		this.repairDays = repairDays;
	}

	public Double getWarAmount() {
		return warAmount;
	}

	public void setWarAmount(Double warAmount) {
		this.warAmount = warAmount;
	}

	public TtAsRepairOrderPO getRepaitPo() {
		return repairPo;
	}

	public void setRepaitPo(TtAsRepairOrderPO repaitPo) {
		this.repairPo = repaitPo;
	}


}
