//加载省市县
var regionData=null;
function addRegionInit(){
	var url=globalContextPath+"/crm/data/DataManage/initRegionData.json";
	makeSameFormCall(url, regionResult, "fm") ;
	loadcalendar();
	doCusChange();
}
function regionResult(json){
	regionData=json.dataList;
	var provinceId=null;
	if($("provinceId")!=null){
		provinceId=$("provinceId").value;
	}
	var cityId=null;
	if($("cityId")!=null){
		cityId=$("cityId").value;
	}
	var townId=null;
	if($("townId")!=null){
		townId=$("townId").value;
	}
	genRegion('dPro','dCity','dArea',provinceId,cityId,townId);
}

//*****************订单提报加载省市区start****************/
//加载省市县
function orderRegionInit(){
	var url=globalContextPath+"/sales/ordermanage/orderreport/UrgentOrderReport/initRegionData.json";
	makeSameFormCall(url, orderRegionResult, "fm") ;
	//loadcalendar();
	//doCusChange();
}
function orderRegionResult(json){
	regionData=json.dataList;
	var provinceId=null;
	if($("provinceId")!=null){
		provinceId=$("provinceId").value;
	}
	var cityId=null;
	if($("cityId")!=null){
		cityId=$("cityId").value;
	}
	var townId=null;
	if($("townId")!=null){
		townId=$("townId").value;
	}
	genRegion('txt1','txt2','txt3',provinceId,cityId,townId);
}
//*****************订单提报加载省市区end****************/

//*****************节能惠民star**********************/
function jn_addRegionInit(){
	
var url=globalContextPath+"/crm/data/DataManage/initRegionData1.json";
makeSameFormCall(url, jn_regionResult, "fm") ;
loadcalendar();
doCusChange();
}

function jn_regionResult(json){
	regionData=json.dataList;
	var provinceId=null;
	if($("provinceId")!=null){
		provinceId=$("provinceId").value;
	}
	var cityId=null;
	if($("cityId")!=null){
		cityId=$("cityId").value;
	}
	var townId=null;
	if($("townId")!=null){
		townId=$("townId").value;
	}
	genRegion('dPro','dCity','dTown',provinceId,cityId,townId);
}

function jn_addRegionInit2(){
	
	var url=globalContextPath+"/crm/data/DataManage/initRegionData1.json";
	makeSameFormCall(url, jn_regionResult2, "fm") ;
	loadcalendar();
	doCusChange();
}

function jn_regionResult2(json){
	regionData=json.dataList;
	var provinceId=null;
	if($("c_provinceId")!=null){
		provinceId=$("c_provinceId").value;
	}
	var cityId=null;
	if($("c_cityId")!=null){
		cityId=$("c_cityId").value;
	}
	var townId=null;
	if($("c_townId")!=null){
		townId=$("c_townId").value;
	}
	genRegion('cPro','cCity','cTown',provinceId,cityId,townId);
}

//*****************节能惠民end**********************/

//*****************实销上报star**********************/
function sx_addRegionInit(){
	
var url=globalContextPath+"/crm/data/DataManage/initRegionData1.json";
makeSameFormCall(url, sx_regionResult, "fm") ;
loadcalendar();
doCusChange();
}

function sx_regionResult(json){
	regionData=json.dataList;
	var provinceId=null;
	if($("provinceId")!=null){
		provinceId=$("provinceId").value;
	}
	var cityId=null;
	if($("cityId")!=null){
		cityId=$("cityId").value;
	}
	var townId=null;
	if($("townId")!=null){
		townId=$("townId").value;
	}
	genRegion('txt1','txt2','txt3',provinceId,cityId,townId);
}

function sx_addRegionInit2(){
	
	var url=globalContextPath+"/crm/data/DataManage/initRegionData1.json";
	makeSameFormCall(url, sx_regionResult2, "fm") ;
	loadcalendar();
	doCusChange();
}

function sx_regionResult2(json){
	regionData=json.dataList;
	var provinceId=null;
	if($("c_provinceId")!=null){
		provinceId=$("c_provinceId").value;
	}
	var cityId=null;
	if($("c_cityId")!=null){
		cityId=$("c_cityId").value;
	}
	var townId=null;
	if($("c_townId")!=null){
		townId=$("c_townId").value;
	}
	
	genRegion('txt4','txt5','txt6',provinceId,cityId,townId);
}

//*****************实销上报end**********************/

function genRegion(proId,cityId,areaId){ //生成省份，城市，县
	
	//生成省份
	_gencPro(proId, arguments[3]);
	//生成城市
	_regionCity($(proId), cityId, arguments[4]);
	//生成县
	if(areaId != '')_regionCity($(cityId), areaId, arguments[5]);
}


function _gencPro(proId){ //生成省份
	addDefaultOpts(proId);
	var opt;
	for(var i=0;i<regionData.length;i++){
		if(regionData[i].REGION_TYPE == "10541002"||regionData[i].REGION_TYPE == "10541001"){
			opt = addOption($(proId), regionData[i].REGION_NAME, regionData[i].REGION_CODE);
			if(arguments[1] && arguments[1] == regionData[i].REGION_CODE) opt.selected = "selected";
		}
	}
}

function _regionCity(obj,cityId){ //生成城市(县)
	addDefaultOpts(cityId);
	var opt;
	for(var i=0;i<regionData.length;i++){
		if(regionData[i].PAR_CODE == obj.value){
			opt = addOption($(cityId), regionData[i].REGION_NAME, regionData[i].REGION_CODE);
			if(arguments[2] && arguments[2] == opt.value) opt.selected = "selected";
		}
	}
}

function addDefaultOpts(locId){ //添加默认的"-请选择-"选项
	$(locId).options.length = 0;
	addOption($(locId), "-请选择-", "");
}