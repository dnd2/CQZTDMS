package com.infodms.dms.common.component;

import java.util.Map;

import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.common.component.dict.RegionDict;
import com.infoservice.mvc.component.Component;

public class DictComponent implements Component{

	public void destroy() {
		
	}

	public void init(Map<String, String> params) {
		CodeDict.getInstance().init();
		RegionDict.getInstance().init();
	}

}
