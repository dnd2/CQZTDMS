package com.infodms.dms.base;

import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;

public abstract class ActionBase {
	public String getJs(RequestWrapper request) {
		String fileName = CommonUtils.checkNull(request.getParamValue("fileName"));
		String url = "/js/action/" + this.getClass().getPackage().getName() + "." + fileName + ".js";
		return url;
	}

}
