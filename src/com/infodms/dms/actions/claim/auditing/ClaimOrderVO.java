package com.infodms.dms.actions.claim.auditing;

import java.util.List;

import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infoservice.po3.bean.PO;

/**
 * 索赔申请单详细信息
 * @author XZM
 */
public class ClaimOrderVO {
	/** 索赔申请单信息 */
	private TtAsWrApplicationPO claimPO;
	/** 车辆信息 */
	private TmVehiclePO vehiclePO;
	/** 索赔单对应配件信息(全部) */
	private List<PO> partsList;
	/** 索赔单对应工时信息(全部) */
	private List<PO> laborList;
	/** 经销商信息 */
	private TmDealerPO dealerPO;
	/** 主要工时代码 */
	@Deprecated
	private String mainLaborCode;
	/** 主要配件代码 */
	@Deprecated
	private String mainPartCode;
	/** 主要工时列表 */
	private List<PO> mainLabourList;
	/** 主要配件列表 */
	private List<PO> mainPartList;
	/** 索赔工时车型组代码 */
	private String wrGroupCode;
	/** 索赔工时车型组ID */
	private String wrGroupId;
	/** 索赔工时车型组名称 */
	private String wrGroupName;
	/** 索赔配件车型组代码 */
	private String wrPartGroupCode;
	/** 索赔配件车型组ID */
	private String wrPartGroupId;
	/** 所属公司（微车/轿车）*/
	private Long companyId;

	public TtAsWrApplicationPO getClaimPO() {
		return claimPO;
	}

	public void setClaimPO(TtAsWrApplicationPO claimPO) {
		this.claimPO = claimPO;
	}

	public TmVehiclePO getVehiclePO() {
		return vehiclePO;
	}

	public void setVehiclePO(TmVehiclePO vehiclePO) {
		this.vehiclePO = vehiclePO;
	}

	public List<PO> getPartsList() {
		return partsList;
	}

	public void setPartsList(List<PO> partsList) {
		this.partsList = partsList;
	}

	public List<PO> getLaborList() {
		return laborList;
	}

	public void setLaborList(List<PO> laborList) {
		this.laborList = laborList;
	}

	public TmDealerPO getDealerPO() {
		return dealerPO;
	}

	public void setDealerPO(TmDealerPO dealerPO) {
		this.dealerPO = dealerPO;
	}

	@Deprecated
	public String getMainLaborCode() {
		return mainLaborCode;
	}

	@Deprecated
	public void setMainLaborCode(String mainLaborCode) {
		this.mainLaborCode = mainLaborCode;
	}

	@Deprecated
	public String getMainPartCode() {
		return mainPartCode;
	}

	@Deprecated
	public void setMainPartCode(String mainPartCode) {
		this.mainPartCode = mainPartCode;
	}

	public String getWrGroupCode() {
		return wrGroupCode;
	}

	public void setWrGroupCode(String wrGroupCode) {
		this.wrGroupCode = wrGroupCode;
	}

	public String getWrGroupId() {
		return wrGroupId;
	}

	public void setWrGroupId(String wrGroupId) {
		this.wrGroupId = wrGroupId;
	}

	public String getWrPartGroupCode() {
		return wrPartGroupCode;
	}

	public void setWrPartGroupCode(String wrPartGroupCode) {
		this.wrPartGroupCode = wrPartGroupCode;
	}

	public String getWrPartGroupId() {
		return wrPartGroupId;
	}

	public void setWrPartGroupId(String wrPartGroupId) {
		this.wrPartGroupId = wrPartGroupId;
	}

	public List<PO> getMainLabourList() {
		return mainLabourList;
	}

	public void setMainLabourList(List<PO> mainLabourList) {
		this.mainLabourList = mainLabourList;
	}

	public List<PO> getMainPartList() {
		return mainPartList;
	}

	public void setMainPartList(List<PO> mainPartList) {
		this.mainPartList = mainPartList;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		if(companyId!=null)
			this.companyId = companyId;
		else
			this.companyId = new Long(-1);
	}

	public String getWrGroupName() {
		return wrGroupName;
	}

	public void setWrGroupName(String wrGroupName) {
		this.wrGroupName = wrGroupName;
	}
	
}
