package com.infodms.dms.common;

import java.util.HashMap;

public class PopSelect extends HashMap<String, String>{
	
    public PopSelect(){
        this.put("moduleSQL", "select FUNC_ID as ID,FUNC_NAME as DESCR,FUNC_NAME from TC_FUNC where 1=1");
        this.put("subModuleSQL", "select FUNC_ID as ID,FUNC_NAME as DESCR,FUNC_NAME from TC_FUNC where 1=1 and PAR_FUNC_ID=?");
        this.put("regionSQL", "select REGION_CODE as ID,REGION_NAME as DESCR from TM_REGION where REGION_TYPE="+Constant.REGION_TYPE_02+"");
        this.put("dealerSQL", "select A.DEALER_ID as ID,A.DEALER_NAME as DESCR from TM_DEALER A,TM_REGION C where A.PROVINCE_ID=C.REGION_CODE AND A.DEALER_LEVEL="+Constant.DEALER_LEVEL_01+" and A.STATUS="+Constant.STATUS_ENABLE+" and C.STATUS="+Constant.STATUS_ENABLE+" and A.DEALER_TYPE="+Constant.DEALER_TYPE_DWR+" and A.PROVINCE_ID=?  order by A.DEALER_ID");
    }
	
}
