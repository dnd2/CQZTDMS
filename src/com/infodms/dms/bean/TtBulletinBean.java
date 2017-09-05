/**********************************************************************
 * <pre>
 * FILE : TtBulletinBean.java
 * CLASS : TtBulletinBean
 *
 * AUTHOR : zxy
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-16|zxy| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.bean;


public class TtBulletinBean {

	private Integer status;
	private Long typeId;
	private String typename;
	private Long updateBy;
	private Integer isReplay;
	private String createBy;
	private String content;
	private Long bulletinId;
	private String createDate;
	private String topic;
	private String remark;
	private String isRade;

	private Long dealerId;
	private Long orgId;
	private String dealerCode;
	private String dealerName;
	private String orgName;
	private Integer totalNum;
	private Integer notReadNum;
	private String name;
	private String poseName;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Integer getIsReplay() {
		return isReplay;
	}
	public void setIsReplay(Integer isReplay) {
		this.isReplay = isReplay;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getBulletinId() {
		return bulletinId;
	}
	public void setBulletinId(Long bulletinId) {
		this.bulletinId = bulletinId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getIsRade() {
		return isRade;
	}
	public void setIsRade(String isRade) {
		this.isRade = isRade;
	}
	public Long getDealerId() {
		return dealerId;
	}
	public void setDealerId(Long dealerId) {
		this.dealerId = dealerId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getNotReadNum() {
		return notReadNum;
	}
	public void setNotReadNum(Integer notReadNum) {
		this.notReadNum = notReadNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPoseName() {
		return poseName;
	}
	public void setPoseName(String poseName) {
		this.poseName = poseName;
	}
	
}

