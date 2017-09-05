//动态改变库区下拉框的值(库道)
function areaCheckBox() {
	var yieldly = document.getElementById("YIELDLY").value;
	var area_obj = document.getElementById("AREA_NAME");
	var road_obj = document.getElementById("ROAD_NAME");
	var sit_obj = document.getElementById("SIT_NAME");
	if(road_obj!=null){
		// 只要库区发生改变的时候库位都清空！
		road_obj.options.length = 0;
		road_obj.options[0] = new Option("-请选择-", "");
	}
	if(sit_obj!=null){
		// 只要库区发生改变的时候库位都清空！
		sit_obj.options.length = 0;
		sit_obj.options[0] = new Option("-请选择-", "");
	}
	if (yieldly == "") {
		area_obj.options.length = 0;
		area_obj.options[0] = new Option("-请选择-", "");
	} else {
		var url = g_webAppName+"/sales/storage/storagemanage/VehiclePicture/getYieldlyIdByArea.json";
		makeCall(url, showAreaList, {
			YIELDLY : yieldly
		});

	}
}
function showAreaList(json) {
	var obj = document.getElementById("AREA_NAME");
	obj.options.length = 0;
	obj.options[0] = new Option("-请选择-", "");
	for ( var i = 0; i < json.areaList.length; i++) {
		obj.options[i + 1] = new Option(json.areaList[i].AREA_NAME,
				json.areaList[i].AREA_ID + "");

	}
}
//动态改变库道下拉框的值(库道)
function roadCheckBox(proStatus) {
	var areaId = document.getElementById("AREA_NAME").value;
	var road_obj = document.getElementById("ROAD_NAME");
	var sit_obj = document.getElementById("SIT_NAME");
	if(sit_obj!=null){
		// 只要库区发生改变的时候库位都清空！
		sit_obj.options.length = 0;
		sit_obj.options[0] = new Option("-请选择-", "");
	}
	// 只要库区ID不为空时候库道才清空！
	if (areaId == "") {
		road_obj.options.length = 0;
		road_obj.options[0] = new Option("-请选择-", "");
	} else {
		var url = g_webAppName+"/sales/storage/storagemanage/VehiclePicture/getAreaIdByRoad.json";
		makeCall(url, showRoadList, {
			AREA_NAME : areaId,
			proStatus:proStatus
		});

	}
}
function showRoadList(json) {
	var obj = document.getElementById("ROAD_NAME");
	obj.options.length = 0;
	obj.options[0] = new Option("-请选择-", "");
	for ( var i = 0; i < json.roadList.length; i++) {
		obj.options[i + 1] = new Option(json.roadList[i].ROAD_NAME,
				json.roadList[i].ROAD_ID + "");

	}
}
// 动态改变库位下拉框的值
function sitCheckBox(proStatus) {
	var areaId = document.getElementById("AREA_NAME").value;
	var roadId = document.getElementById("ROAD_NAME").value;
	var sit_obj = document.getElementById("SIT_NAME");
	if (roadId == "") {
		sit_obj.options.length = 0;
		sit_obj.options[0] = new Option("-请选择-", "");
	} else {
		var url =g_webAppName+"/sales/storage/storagemanage/VehiclePicture/getRoadIdBySit.json";
		makeCall(url, showSitList, {
			ROAD_NAME : roadId,
			proStatus:proStatus
		});
	}
}
function showSitList(json) {
	var obj = document.getElementById("SIT_NAME");
	obj.options.length = 0;
	obj.options[0] = new Option("-请选择-", "");
	for ( var i = 0; i < json.sitList.length; i++) {
		obj.options[i + 1] = new Option(json.sitList[i].SIT_NAME,
				json.sitList[i].SIT_ID + "");

	}
}
//初始化下拉框值 （修改用）
function doInitCombo(areaId,areaValue,roadId,roadValue,sitId,sitValue){
	var area_obj = document.getElementById("AREA_NAME");
	var road_obj = document.getElementById("ROAD_NAME");
	var sit_obj = document.getElementById("SIT_NAME");
	if(area_obj!=null && areaId!="" && areaValue!=""){
		// 只要库区发生改变的时候库位都清空！
		area_obj.options.length = 0;
		area_obj.options[0] = new Option(areaValue, areaId+"");
		area_obj.options[1] = new Option("-请选择-", "");
		
	}
	if(road_obj!=null && roadId!="" && roadValue!=""){
		// 只要库区发生改变的时候库位都清空！
		road_obj.options.length = 0;
		road_obj.options[0] = new Option(roadValue, roadId+"");
		road_obj.options[1] = new Option("-请选择-", "");
		
	}
	if(sit_obj!=null && sitId!="" && sitValue!=""){
		// 只要库区发生改变的时候库位都清空！
		sit_obj.options.length = 0;		
		sit_obj.options[0] = new Option(sitValue, sitId+"");
		sit_obj.options[1] = new Option("-请选择-", "");
	}
}