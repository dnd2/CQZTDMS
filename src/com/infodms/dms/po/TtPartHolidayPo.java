package com.infodms.dms.po;

import java.util.Date;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class TtPartHolidayPo extends PO{
    
    private Long holidayId;
    private Date holidayDate;
    private Integer status;
    private Long createBy;
    private Date createDate;
    private Long updateBy;
    private Date updateDate;
    
    public Long getHolidayId() {
        return holidayId;
    }
    public void setHolidayId(Long holidayId) {
        this.holidayId = holidayId;
    }
    public Date getHolidayDate() {
        return holidayDate;
    }
    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Long getCreateBy() {
        return createBy;
    }
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Long getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
}
