var REGION_PROVINCE_TYPE = "10541002"; //省份type
if(typeof(codeData) != "undefined") codeData = codeData.data;
if(typeof(regionData) != "undefined") regionData = regionData.data;

/**
 * 方法说明：
 * 		根据type返回一个List
 * @param type
 * @returns {Array}
 */
function getCodeList(type){ 
	var arr = new Array();
	for(var i=0;i<codeData.length;i++){
		if(codeData[i].type == type){
			arr.push({codeId:codeData[i].codeId,codeDesc:codeData[i].codeDesc});
		}
	}
	return arr;
}

/**
 * 方法说明：
 * 		根据codeId拿到某个属性的codeDesc
 * @param codeId
 * @returns
 */
function getItemValue(codeId){ 
	var itemValue = null;
	for(var i=0;i<codeData.length;i++){
		if(codeData[i].codeId == codeId){
			itemValue = codeData[i].codeDesc;
		}
	}
	return itemValue==null?"":itemValue;
}

/**
 * 方法说明：
 * 		获取select组件默认选中框
 * @returns {String}
 */
function genDefaultOpt(){
	return "<option selected value=''>-请选择-</option>";
}

/**
 * 方法说明：
 * 		根据TC_CODE的TYPE类型生成下拉组件
 * 
 * @param id 
 * 		元素ID
 * @param type 
 * 		数据字典TC_CODE的type值
 * @param selectedKey
 * 		需要默认选中的TC_CODE值
 * @param setAll
 * 		是否支持全部选择 true 是 false 否
 * @param _class_
 * 		下拉框自定义样式
 * @param _script_
 * 		下拉框自定义脚本
 * @param nullFlag
 * 		数据验证脚本
 */
function genSelBox(id,type,selectedKey,setAll,_class_,_script_,nullFlag){ 
	var str = "";
	if(_class_==undefined || _class_ == '') {
		_class_ = 'u-select';
	}
	str += "<select id='" + id + "' name='" + id +"' class='"+_class_+"' " + _script_ ;
	if(nullFlag!=undefined&&nullFlag==true){
		str += " datatype='0,is_null,200' ";
	}
	str += " > ";
	if(setAll){
		str += genDefaultOpt();
	}
	for(var i=0;i<codeData.length;i++){
		if(codeData[i].type == type){
			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
		}
	}
	str += "</select>";
	document.write(str);
}

/**
 * 方法说明：
 * 根据TC_CODE的TYPE类型生成下拉组件
 * 
 * @param id 
 * 		元素ID
 * @param type 
 * 		数据字典TC_CODE的type值
 * @param selectedKey
 * 		需要默认选中的TC_CODE值
 * @param setAll
 * 		是否支持全部选择 true 是 false 否
 * @param _class_
 * 		下拉框自定义样式
 * @param _script_
 * 		下拉框自定义脚本
 * @param nullFlag
 * 		数据验证脚本
 * @param expStr
 * 		需要排除的数据字典项,多个字典用,号分隔
 */
function genSelBoxExp(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
	var str = "";
	var arr;
	if(expStr.indexOf(",")>0)
		arr = expStr.split(",");
	else {
		expStr = expStr+",";
		arr = expStr.split(",");
	}
	if(_class_==undefined || _class_ == '') {
		_class_ = 'u-select';
	}
	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
	if(nullFlag && nullFlag == "true"){
		str += " datatype='0,0,0' ";
	}
	str += " onChange=\"doSelectChange(this.value, 'doCusChange');\"> ";
	if(setAll){
		str += genDefaultOpt();
	}
	for(var i=0;i<codeData.length;i++){
		var flag = true;
		for(var j=0;j<arr.length;j++){
			if(codeData[i].codeId == arr[j]){
				flag = false;
			}
		}
		if(codeData[i].type == type && flag){
			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
		}
	}
	str += "</select>";	
	document.write(str);
}
// genSelBoxExp方法生成的下拉框值改变时调用doCusChange方法的防错处理
function doSelectChange( o, callback ) {
	return callback && typeof window[callback] === 'function' ? window[callback].call(null, o)
			: false;	
}

function genSelBoxStrExp(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
	var str = "";
	var arr;
	if(expStr.indexOf(",")>0)
		arr = expStr.split(",");
	else {
		expStr = expStr+",";
		arr = expStr.split(",");
	}
	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
	if(nullFlag!=undefined&&nullFlag==true){
		str += " datatype='0,is_null,200' ";
	}
	str += " > ";
	if(setAll){
		str += genDefaultOpt();
	}
	for(var i=0;i<codeData.length;i++){
		var flag = true;
		for(var j=0;j<arr.length;j++){
			if(codeData[i].codeId == arr[j]){
				flag = false;
			}
		}
		if(codeData[i].type == type && flag){
			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
		}
	}
	str += "</select>";
	return str;
}

/**
 * 获取地区
 * @param {*} proId 
 * @param {*} cityId 
 * @param {*} areaId 
 */
function genLocSel(proId,cityId,areaId){ //生成省份，城市，县
	//生成省份
	_genPro(proId, arguments[3]);
	//生成城市
	_genCity($('#' + proId)[0], cityId, arguments[4]);
	//生成县
	if(areaId != '')_genCity($('#' + cityId)[0], areaId, arguments[5]);
}

function _genPro(proId){ //生成省份
	addDefaultOpt(proId);
	var opt;
	for(var i=0;i<regionData.length;i++){
		if(regionData[i].regionType == REGION_PROVINCE_TYPE){
			opt = addOption($('#' + proId)[0], regionData[i].regionName, regionData[i].regionCode);
			if(arguments[1] && arguments[1] == regionData[i].regionCode) opt.selected = "selected";
		}
	}
}

function _genCity(obj,cityId){ //生成城市(县)
	addDefaultOpt(cityId);
	var opt;
	for(var i=0;i<regionData.length;i++){
		if(regionData[i].parId == obj.value){
			opt = addOption($('#' + cityId)[0], regionData[i].regionName, regionData[i].regionCode);
			if(arguments[2] && arguments[2] == opt.value) opt.selected = "selected";
		}
	}
}
/**
 * TODO wangsw 可能与 _genCity方法重复
 * @param obj
 * @param cityId
 */
function _regionCity(obj,cityId){ //生成城市(县)
	addDefaultOpt(cityId);
	var opt;
	for(var i=0;i<regionData.length;i++){
		if(regionData[i].parId == obj.value){
			opt = addOption($("#"+cityId)[0], regionData[i].regionName, regionData[i].regionCode);
			if(arguments[2] && arguments[2] == opt.value) opt.selected = "selected";
		}
	}
}


function addDefaultOpt(locId){ //添加默认的"-请选择-"选项
	if(locId && $('#' + locId)[0]){
		$('#' + locId)[0].options.length = 0;
		addOption($('#' + locId)[0], "-请选择-", "");
	}
}

//AJAX 下拉框联动共用方法
function addOption(objSelectNow,txt,val){
	var objOption = document.createElement("OPTION");
	objOption.text = txt;
	objOption.value = val;
	objSelectNow.options.add(objOption);
	return objOption;
}

// 获取省份
function _getPro_(spanPro, spanCity, spanArea, hidPro, hidCity, hidArea, hidProId, hidCityId, hidAreaId, txtClass, txtOther, proValue, cityValue, areaValue) {
	if(!spanCity) {
		spanCity = "" ;
	}
	
	if(!spanArea) {
		spanArea = "" ;
	}
	
	if(!hidCity) {
		hidCity = "" ;
	}
	
	if(!hidArea) {
		hidArea = "" ;
	}
	
	if(!hidProId) {
		hidProId = hidPro ;
	}
	
	if(!hidCityId) {
		hidCityId = hidCity ;
	}
	
	if(!hidAreaId) {
		hidAreaId = hidArea ;
	}
	
	if(!txtClass) {
		txtClass = "" ;
	}
	
	if(!txtOther) {
		txtOther = "" ;
	}
	
	var str = "" ;
	var oSpanPro = document.getElementById(spanPro) ;
	var proOnchange = "" ;
	
	if(spanCity) {
		proOnchange = '_getCity_(\''+spanCity+'\', \''+spanArea+'\', this.id, \''+hidCity+'\', \''+hidArea+'\', \''+hidCityId+'\', \''+hidAreaId+'\', \''+txtClass+'\', \''+txtOther+'\') ;' ;
	}
	
	str = genTextBoxStr('_proName_', '_proId_', hidPro, hidProId, '_proDiv_', txtClass, '', proOnchange, txtOther, _getProName_(), _getProId_()) ;

	oSpanPro.innerHTML = str ;
	
	if(proValue) {
		document.getElementById(hidProId).value = proValue ;
		document.getElementById('_proId_').value = _genValue_(proValue) ;
		document.getElementById('_proDiv_').style.display = "none" ;
	}
	
	if(spanCity) {
		var oPro = document.getElementById(hidProId) ;
		var oSpanCity = document.getElementById(spanCity) ;
		var cityOnchange = "" ;
		
		if(spanArea) {
			cityOnchange = '_getArea_(\''+spanArea+'\', this.id, \''+hidArea+'\', \''+hidAreaId+'\', \''+txtClass+'\', \''+txtOther+'\') ;' ;
		}
		
		str = genTextBoxStr('_cityName_', '_cityId_', hidCity, hidCityId, '_cityDiv_', txtClass, '', cityOnchange, txtOther, _genCityName_(oPro) , _genCityId_(oPro)) ;
	
		oSpanCity.innerHTML = str ;
		
		if(cityValue) {
			document.getElementById(hidCityId).value = cityValue ;
		    document.getElementById('_cityId_').value = _genValue_(cityValue) ;
		    document.getElementById('_cityDiv_').style.display = "none" ;
		}
	}
	
	if(spanArea) {
		var oCity = document.getElementById(hidCityId) ;
		var oSpanArea = document.getElementById(spanArea) ;
		
		str = genTextBoxStr('_areaName_', '_areaId_', hidArea, hidAreaId, '_areaDiv_', txtClass, '', '', txtOther, _genCityName_(oCity) , _genCityId_(oCity)) ;
	
		oSpanArea.innerHTML = str ;
		
		if(areaValue) {
			document.getElementById(hidAreaId).value = areaValue ;
			document.getElementById('_areaId_').value = _genValue_(areaValue) ;
			document.getElementById('_areaDiv_').style.display = "none" ;
		}
	}
}

// 获取城市
function _getCity_(spanCity, spanArea, hidProId, hidCity, hidArea, hidCityId, hidAreaId, txtClass, txtOther) {
	if(!spanArea) {
		spanArea = "" ;
	}
	
	if(!hidArea) {
		hidArea = "" ;
	}
	
	if(!hidCityId) {
		hidCityId = hidCity ;
	}
	
	if(!hidAreaId) {
		hidAreaId = hidArea ;
	}
	
	if(!txtClass) {
		txtClass = "" ;
	}
	
	if(!txtOther) {
		txtOther = "" ;
	}
	
	var oSpanCity = document.getElementById(spanCity) ;
	var oPro = document.getElementById(hidProId) ;
	var cityOnchange = "" ;
	
	if(spanArea) {
		cityOnchange = '_getArea_(\''+spanArea+'\', this.id, \''+hidArea+'\', \''+hidAreaId+'\', \''+txtClass+'\', \''+txtOther+'\') ;' ;
	}
	
	str = genTextBoxStr('_cityName_', '_cityId_', hidCity, hidCityId, '_cityDiv_', txtClass, '', cityOnchange, txtOther, _genCityName_(oPro) , _genCityId_(oPro)) ;

	oSpanCity.innerHTML = str ;
	
	if(spanArea) {
		var oCity = document.getElementById(hidCityId) ;
		var oSpanArea = document.getElementById(spanArea) ;
		
		str = genTextBoxStr('_areaName_', '_areaId_', hidArea, hidAreaId, '_areaDiv_', txtClass, '', '', txtOther, _genCityName_(oCity) , _genCityId_(oCity)) ;
	
		oSpanArea.innerHTML = str ;
	}
}

function _getArea_(spanArea, hidCityId, hidArea, hidAreaId, txtClass, txtOther) {
	var oSpanArea = document.getElementById(spanArea) ;
	var oCity = document.getElementById(hidCityId) ;
	
	if(!hidAreaId) {
		hidAreaId = hidArea ;
	}
	
	str = genTextBoxStr('_areaName_', '_areaId_', hidArea, hidAreaId, 'areaDiv', txtClass, '', '', txtOther, _genCityName_(oCity) , _genCityId_(oCity)) ;

	oSpanArea.innerHTML = str ;
}

/**
 * 根据地级市代码取名称
 * @param regionCode
 * @returns
 */
function getRegionName(regionCode){ 
	var itemValue = null;
	for(var i=0;i<regionData.length;i++){
		if(regionData[i].regionCode == regionCode){
			itemValue= regionData[i].regionName;
		}
	}
	return itemValue==null?"":itemValue;
}

//获取省市县的名字
//拿到地区名字,打印到页面上
function writeRegionName(regionCode){ 
	document.write(getRegionName(regionCode));
}