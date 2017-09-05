package com.infoservice.dms.chana.vo;

import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("serial")
public class TmClaimConfirmBillVO extends BaseVO {
    		private String billNo; // 开票单号
    		
    	 	private String invoiceNo;// 发票号

    	    private Date balanceBegin;// 结算起

    	    private Date balanceEnd;// 结算止

    	    private Integer totalBillSum;// 总申报单据数

    	    private Double cutExpense;// 扣款总费用

    	    private String produceBase;// 生产基地

    	    private Double totalExpense;// 申请总费用

    	    private Double otherExpense;// 其他费用

    	    private Double billAmount;// 开票金额

    	    private String remark;
    	    
    	    private HashMap<Integer,TmClaimReceiveDetailVO> claimReceiveVoList;

			public String getBillNo() {
				return billNo;
			}

			public void setBillNo(String billNo) {
				this.billNo = billNo;
			}

			public String getInvoiceNo() {
				return invoiceNo;
			}

			public void setInvoiceNo(String invoiceNo) {
				this.invoiceNo = invoiceNo;
			}

			public Date getBalanceBegin() {
				return balanceBegin;
			}

			public void setBalanceBegin(Date balanceBegin) {
				this.balanceBegin = balanceBegin;
			}

			public Date getBalanceEnd() {
				return balanceEnd;
			}

			public void setBalanceEnd(Date balanceEnd) {
				this.balanceEnd = balanceEnd;
			}

			public Integer getTotalBillSum() {
				return totalBillSum;
			}

			public void setTotalBillSum(Integer totalBillSum) {
				this.totalBillSum = totalBillSum;
			}

			public Double getCutExpense() {
				return cutExpense;
			}

			public void setCutExpense(Double cutExpense) {
				this.cutExpense = cutExpense;
			}

			public String getProduceBase() {
				return produceBase;
			}

			public void setProduceBase(String produceBase) {
				this.produceBase = produceBase;
			}

			public Double getTotalExpense() {
				return totalExpense;
			}

			public void setTotalExpense(Double totalExpense) {
				this.totalExpense = totalExpense;
			}

			public Double getOtherExpense() {
				return otherExpense;
			}

			public void setOtherExpense(Double otherExpense) {
				this.otherExpense = otherExpense;
			}

			public Double getBillAmount() {
				return billAmount;
			}

			public void setBillAmount(Double billAmount) {
				this.billAmount = billAmount;
			}

			public String getRemark() {
				return remark;
			}

			public void setRemark(String remark) {
				this.remark = remark;
			}

			public HashMap<Integer, TmClaimReceiveDetailVO> getClaimReceiveVoList() {
				return claimReceiveVoList;
			}

			public void setClaimReceiveVoList(
					HashMap<Integer, TmClaimReceiveDetailVO> claimReceiveVoList) {
				this.claimReceiveVoList = claimReceiveVoList;
			}

}
