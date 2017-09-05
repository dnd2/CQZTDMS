package com.infoservice.dms.chana.vo;

@SuppressWarnings("serial")
public class TmClaimReceiveDetailVO extends BaseVO {
	 	private String license;// 车牌号

	    private String claimNo;// 索赔单号

	    private String roNo;// 工单号
	    
	    private String dmsRoNo;// 下端工单号

	    private String vin;

	    private String remark;

		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}

		public String getClaimNo() {
			return claimNo;
		}

		public void setClaimNo(String claimNo) {
			this.claimNo = claimNo;
		}

		public String getRoNo() {
			return roNo;
		}

		public void setRoNo(String roNo) {
			this.roNo = roNo;
		}

		public String getVin() {
			return vin;
		}

		public void setVin(String vin) {
			this.vin = vin;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getDmsRoNo() {
			return dmsRoNo;
		}

		public void setDmsRoNo(String dmsRoNo) {
			this.dmsRoNo = dmsRoNo;
		}
}
