package com.infodms.dms.po;

import com.infoservice.po3.bean.PO;

@SuppressWarnings("serial")
public class VwMaterialInfoPO extends PO{
	private Long seriesId; 
	private String seriesCode;
	private String seriesName;
	private String intentSeries;
	private Long modelId;
	private String modelCode;
	private String modelName;
	private Long pzId;
	private String pzCode;
	private String pzName;
	private String pzIntentSeries;
	private Long materialId;
	private String materialCode;
	private String materialName;  
	private String colorCode;
	private String colorName;
	
	public void setSeriesId(Long seriesId){
		this.seriesId = seriesId;
	}
	
	public Long getSeriesId(){
		return seriesId;
	}
	
	
	public void setSeriesCode(String seriesCode){
		this.seriesCode = seriesCode;
	}
	
	public String getSeriesCode(){
		return seriesCode;
	}
	
	
	
	public void setSeriesName(String seriesName){
		this.seriesName = seriesName;
	}
	
	public String getSeriesName(){
		return seriesName;
	}
	
	
	
	public void setIntentSeries(String intentSeries){
		this.intentSeries = intentSeries;
	}
	
	public String getIntentSeries(){
		return intentSeries;
	}
	
	
	public void setModelId(Long modelId){
		this.modelId = modelId;
	}
	
	public Long getModelId(){
		return modelId;
	}
	
	
	public void setModelCode(String modelCode){
		this.modelCode = modelCode;
	}
	
	public String getModelCode(){
		return modelCode;
	}
	
	public void setModelName(String modelName){
		this.modelName = modelName;
	}
	
	public String getModelName(){
		return modelName;
	}
	
	public void setPzId(Long pzId){
		this.pzId = pzId;
	}
	
	public Long getPzId(){
		return pzId;
	}
	
	public void setPzCode(String pzCode){
		this.pzCode = pzCode;
	}
	
	public String getPzCode(){
		return pzCode;
	}
	
	public void setPzName(String pzName){
		this.pzName = pzName;
	}
	
	public String getPzName(){
		return pzName;
	}
	
	public void setPzIntentSeries(String pzIntentSeries){
		this.pzIntentSeries = pzIntentSeries;
	}
	
	public String getPzIntentSeries(){
		return pzIntentSeries;
	}
	
	
	public void setMaterialId(Long materialId){
		this.materialId = materialId;
	}
	
	public Long getMaterialId(){
		return materialId;
	}
	
	
	public void setMaterialCode(String materialCode){
		this.materialCode = materialCode;
	}
	
	public String getMaterialCode(){
		return materialCode;
	}
	
	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}
	
	public String getMaterialName(){
		return materialName;
	}
	
	public void setColorName(String colorName){
		this.colorName = colorName;
	}
	
	public String getColorName(){
		return colorName;
	}
}
