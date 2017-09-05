package com.infodms.dms.common.tasktiming;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.RegionBean;
import com.infodms.dms.common.FileConstant;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.common.component.dict.RegionDict;
import com.infoservice.filestore.FileStore;
import com.infoservice.schedule.Task;

public class RegionDictTask extends Task{
	private static Logger logger = Logger.getLogger(RegionDictTask.class);
	
	@Override
	public String execute() {
		try{
			List<RegionBean> codeList = null;
			if(RegionDict.dt==null || RegionDict.hasCodeUpdate()){
				codeList = RegionDict.selRegionList();
			}
			byte[] codeJson = null;
			if(codeList != null){
				codeJson = CodeDict.toJsonFormat("var regionData = ", codeList);
			}
			String fid = null;
			if(codeJson != null){
				fid = FileStore.getInstance().write("regionData.js",
						codeJson);
			}
			String codeJsUrl = null;
			if(fid != null){
				codeJsUrl = FileStore.getInstance().getDomainURL(fid);
				FileConstant.regionJsUrl = codeJsUrl;
				RegionDict.dt = new Date();
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
}
