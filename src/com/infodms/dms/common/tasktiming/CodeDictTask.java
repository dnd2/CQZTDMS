package com.infodms.dms.common.tasktiming;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.CodeBean;
import com.infodms.dms.common.FileConstant;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infoservice.filestore.FileStore;
import com.infoservice.schedule.Task;

public class CodeDictTask extends Task{
	private static Logger logger = Logger.getLogger(CodeDictTask.class);
	
	@Override
	public String execute() {
		try{
			List<CodeBean> codeList = null;
			if(CodeDict.dt==null || CodeDict.hasCodeUpdate()){
				CodeDict.setDictMap();
				logger.info("数据字典写入了内在中,大小:"+CodeDict.dictMap.size());
				
				codeList = CodeDict.selCodeList();
			}
			byte[] codeJson = null;
			if(codeList != null){
				codeJson = CodeDict.toJsonFormat("var codeData = ", codeList);
			}
			String fid = null;
			if(codeJson != null){
				fid = FileStore.getInstance().write("codeData.js",
						codeJson);
			}
			String codeJsUrl = null;
			if(fid != null){
				codeJsUrl = FileStore.getInstance().getDomainURL(fid);
				FileConstant.codeJsUrl = codeJsUrl;
				logger.info("codeJsUrl"+FileConstant.codeJsUrl);
				CodeDict.dt = new Date();
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
}
