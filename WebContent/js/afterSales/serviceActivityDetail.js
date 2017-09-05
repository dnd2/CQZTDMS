
var myPage;
var btnIds = new Array("userQuery","add");
var menuUrl = globalContextPath+"/AfterSales/ServiceActivity/ServiceActivityAction/serviceActivityPageInit.do";
var saveUrl = globalContextPath + "/AfterSales/ServiceActivity/ServiceActivityAction/saveData.json";

$J(document).ready(function(){
	loadcalendar();
	rowNumComputing();
});
// 保存信息反馈类型
function saveData(status) {
	if($J("#activity_strate_date").val()!=null && $J("#activity_strate_date").val()!='' && $J("#activity_end_date").val()!=''  && $J("#activity_end_date").val()!=null ){
		var beginDate=$J("#activity_strate_date").val();  
		var endDate=$J("#activity_end_date").val();  
		var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
		var d2 = new Date(endDate.replace(/\-/g, "\/"));  
		if(d1 > d2){
			MyAlert("活动开始日期不能大于活动结束日期！");
			return;
		}
	}
	
	if($J("#activity_strate_mileage").val()!=null && $J("#activity_strate_mileage").val()!="" && $J("#activity_end_mileage").val()!=null && $J("#activity_end_mileage").val()!=""){
		
		if(parseFloat($J("#activity_strate_mileage").val()) > parseFloat($J("#activity_end_mileage").val())){
			MyAlert("开始里程数不能大于结束里程数！");
			return;
		}
	}
	
	
	if($J("#activity_sales_strate_date").val()!=null && $J("#activity_sales_strate_date").val()!='' && $J("#activity_sales_end_date").val()!=''  && $J("#activity_sales_end_date").val()!=null ){
		var beginDate=$J("#activity_sales_strate_date").val();  
		var endDate=$J("#activity_sales_end_date").val();  
		var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
		var d2 = new Date(endDate.replace(/\-/g, "\/"));  
		if(d1 > d2){
			MyAlert("开始实销日期不能大于结束实销日期！");
			return;
		}
	}
	
	if($J("#activity_strate_mileage").val()!="" || $J("#activity_sales_strate_date").val()!="" || $J("#MODEL_ID11").val() != ""){
		if($J("#dealer_id").val() == '' || $J("#dealer_id").val() == null){
			MyAlert("里程设置、车型选择、实销日期 任意一个及其以上填写了数据 ，那么保存的时候验证下发经销商必选！");
			return;
		}
	}
	
	
	
	var obj = document.getElementsByName("workCodeId");
	if(obj.length == 1){
		MyAlert("工时代码不能为空");
		return;
	}else{
		for(var i=0;i<obj.length-1;i++){
			var value = obj[i].value;
			if(value == null || value == ''){
				MyAlert("工时代码不能为空");
				return;
			}
		}
	}
	var obj = document.getElementsByName("partsCodeId");
	var exchangeNum = document.getElementsByName("exchangeNum");
	var is_recovery = document.getElementsByName("IS_RECOVERY");
	
	//故障模式
	var failureModelNames = document.getElementsByName("failureModelName");
	//故障部位
	var failureLocations = document.getElementsByName("failureLocation");
	//责任供应商
	var responsibilityFactoryNames = document.getElementsByName("responsibilityFactoryName");
	//索赔供应商
	var compensateFactoryNames = document.getElementsByName("compensateFactoryName");
    for(var i=0;i<obj.length-1;i++){
        var value = obj[i].value;
        var num = exchangeNum[i].value;
        var isRecovery = is_recovery[i].value;
        var failureModelName = failureModelNames[i].value;
        var failureLocation = failureLocations[i].value;
        var responsibilityFactoryName = responsibilityFactoryNames[i].value;
        var compensateFactoryName = compensateFactoryNames[i].value;
	    	if(value != null && value != ''){
	    		if(failureModelName == null || failureModelName == ''){
	    			MyAlert("备件编码不为空时，故障部位不能为空！");
	    			return;
	    		}
	    		if(failureLocation == null || failureLocation == ''){
	    			MyAlert("备件编码不为空时，故障模式不能为空！");
	    			return;
	    		}
	    		if(num == null || num == ''){
	    			MyAlert("备件编码不为空时，换件数量不能为空！");
	    			return;
	    		}
	    		if(isRecovery == null || isRecovery == ''){
	    			MyAlert("备件编码不为空时，是否回收必选！");
	    			return;
	    		}
	    		if(isRecovery == 10041001){
	    			if(responsibilityFactoryName == null || responsibilityFactoryName == ''){
	    				MyAlert("备件选择回收时，责任供应商不能为空！");
	    				return;
	    			}
	    			if(compensateFactoryName == null || compensateFactoryName == ''){
	    				MyAlert("备件选择回收时，索赔供应商不能为空！");
	    				return;
	    			}
	    		}
	        }
    }
	if($J("#activity_name").val()=='' || $J("#activity_name").val()==null){
		MyAlert("活动名称不能为空！");
		return;
	}
	if($J("#activity_code").val()=='' || $J("#activity_code").val()==null){
		MyAlert("活动编号不能为空！");
		return;
	}
	if($J("#activity_strate_date").val()=='' || $J("#activity_strate_date").val()==null){
		MyAlert("活动开始日期不能为空！");
		return;
	}
	if($J("#activity_end_date").val()=='' || $J("#activity_end_date").val()==null){
		MyAlert("活动结束日期不能为空！");
		return;
	}
	if($J("#fault_phenomenon").val()=='' || $J("#fault_phenomenon").val()==null){
		MyAlert("活动说明！");
		return;
	}
	if($J("#reason_analysis").val()=='' || $J("#reason_analysis").val()==null){
		MyAlert("申报说明！");
		return;
	}
	if(status==1){
		$J("#activity_status").val("10681001");
		 MyConfirm("确定保存吗？",EditAsDo);
	}else{
		$J("#activity_status").val("10681002");
		 MyConfirm("确定发布吗？",EditAsDo);
	}
	
}
function EditAsDo(){
	var tUrl = saveUrl;
	sendAjax(tUrl, showResult, 'fm');
}
function showResult(json){
	if(json.message == "1"){
		alert("保存成功！");
		backInit();
	}else if(json.message == "2"){
		alert("发布成功！");
		backInit();
	}else {
		alert(json.message);
	}
}

function backInit(){
	window.location.href = menuUrl;
}

function back(){
	var id = $J("#activityId").val();
	window.location.href = menuUrl+"?id="+id;
}

/**
 * 通用选择车型
 * inputId   : 回填页车型名称域
 * inputName ：回填页供应商id域id
 * isMulti   : true值多选，否则单选
 */
function reChoose(inputId,inputCode,isMulti){
      var  idVal=document.getElementById(inputId).value;
      var url=globalContextPath+"/jsp/AfterSales/base/PackStrategy/ChooseModel.jsp?idVal="+idVal+"&INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti;
	  OpenHtmlWindow(url,730,390);
}

function setValue(inputId,inputName,id,name){
	document.getElementById(inputId).value=id;
    document.getElementById(inputName).value=name;
}

function setMoreValue(inputId,inputName,userIdsNames){
	var ids="";
	var names="";
	for (var i=0;i<userIdsNames.length;i++){
        strTemp = userIdsNames[i].toString();
        //定义一数组
        strsTemp = strTemp.split("@@"); //字符分割
        var id = strsTemp[1];
        var name = strsTemp[0];
        if(i>0){
        	ids+=",";
        	names+=",";
        }
        ids+=id;
        names+=name;
	}
	document.getElementById(inputId).value=ids;
	document.getElementById(inputName).value=names;
}
	

function setDealerValue(inputId,inputName,id,name){
	document.getElementById(inputId).value=id;
    document.getElementById(inputName).value=name;
}

function setMoreDealerValue(inputId,inputName,userIdsNames,type){
    //定义一数组
    var strsTemp = userIdsNames.split("@@"); //字符分割
    var id = strsTemp[1];
    var name = strsTemp[0];
    var code = strsTemp[2];
	if(type == 1){
		var values = document.getElementById(inputId).value;
		values += id + ",";
		document.getElementById(inputId).value = values;//重新赋值
		$J("#tr").after('<tr id="tr'+id+'" style="BACKGROUND-COLOR: #fdfdfd" class="table_list_row1">'
				        +'<td align="center" colspan="1" nowrap="nowrap" class = "dealerNum"></td>'
				        +'<td align="center" colspan="2" nowrap="nowrap">'+ code + '</td>'
				        +'<td align="center" colspan="2" nowrap="nowrap">'+ name + '</td>'
				        +'<td align="center" colspan="1" nowrap="nowrap"><input onclick="deleteRow('+id+',\''+inputId+'\');" value="删除" type="button" class="normal_btn"/></td></tr>');
		//设置行号
		setDealerNum();
	}else{
		deleteRow(id,inputId);
	}
}
	//设置行号
	function setDealerNum(){
		var dealerNum = document.getElementsByClassName("dealerNum");//模板行号
		for (var i = 0 ;i < dealerNum.length ; i++) {
			dealerNum[i].innerHTML = i+1;
		}
	}

	//经销商删除行并且重新赋值
	function deleteRow(id,inputId){
		var values = document.getElementById(inputId).value;
		values = values.replace(id+",","");
		document.getElementById(inputId).value = values;//重新赋值
		//删除行
		$J("#tr"+id).remove();
		//设置行号
		setDealerNum();
	}

	function addDealer(inputId,inputCode,isMulti){
		var  idVal=document.getElementById(inputId).value;
		var url=globalContextPath+"/jsp/AfterSales/serviceActivity/showDealer.jsp?idVal="+idVal+"&INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti;
		OpenHtmlWindow(url,730,390);
	}
	
	function checkVin(status){
		var  activityId=document.getElementById("activityId").value;
		var url=globalContextPath+"/jsp/AfterSales/serviceActivity/checkVin.jsp?activityId="+activityId+"&status="+status;
		OpenHtmlWindow(url,730,390);
	}
	
	
	
	function trInit(){
		var tmpTr = document.getElementById("modelDiv").getElementsByTagName("tr")[0];//模板节点
		var tbody = document.getElementById("realTab");//插入节点
		var newRow = tmpTr.cloneNode(true);
		tbody.appendChild(newRow);
		rowNumComputing();	
		}
	function addRow(){
		trInit();
	}
	
	
	function rowNumComputing(){
		var orderNums = document.getElementsByClassName("orderNum");//模板行号
		for (var i = 0 ;i < orderNums.length ; i++) {
			orderNums[i].innerHTML = i+1;
			updatePropertyValueOfFailureLocation(i);
			updatePropertyValueOfFailureModel(i);
			updatePropertyValueOfWorkCode(i);
			updatePropertyValueOfPartsCode(i);
			
			updatePropertyValueOfResponsibilityFactory(i);
			updatePropertyValueOfCompensateFactory(i);
			updateZWRows(i);//总成分类
		}
	}
	

	function updatePropertyValueOfResponsibilityFactory(num){
		//责任件厂商
		var responsibilityFactoryName = document.getElementsByName("responsibilityFactoryName");
		var name = "responsibilityFactoryName"+num; //名称
		
		var responsibilityFactoryId = document.getElementsByName("responsibilityFactoryId");//ID隐藏域
		var id = "responsibilityFactoryId"+num;//ID
		responsibilityFactoryName[num].setAttribute("id",name);
		responsibilityFactoryId[num].setAttribute("id",id);
		var responsibilityFactoryBtn = document.getElementsByName("responsibilityFactoryBtn");
		responsibilityFactoryBtn[num].onclick=function(){
			//备件id
			var codeId = $J("#partsCodeId"+num).val();
				if(codeId == null || codeId == ''){
					MyAlert("请先选备件");
				}else{
					showFactory(id,name,'responsibilityFactoryCode',codeId,'false');
			}
		};
	}
	
 	/* function showFactory(id,name,type,isMulti){
		var  idVal=document.getElementById(id).value;
	    var url=globalContextPath+"/jsp/AfterSales/serviceActivity/addFactory.jsp?idVal="+idVal+"&INPUTCODE="+name+"&INPUTID="+id+"&ISMULTI="+isMulti+"&type="+type;
		OpenHtmlWindow(url,730,390);
	}  */
	
	
	
	function updatePropertyValueOfCompensateFactory(num){
		//索赔厂商
		var compensateFactoryName = document.getElementsByName("compensateFactoryName");
		var name = "compensateFactoryName"+num; //名称
		
		var compensateFactoryId = document.getElementsByName("compensateFactoryId");//ID隐藏域
		var id = "compensateFactoryId"+num;//ID
		
		compensateFactoryName[num].setAttribute("id",name);
		compensateFactoryId[num].setAttribute("id",id);
		var compensateFactoryBtn = document.getElementsByName("compensateFactoryBtn");
		compensateFactoryBtn[num].onclick=function(){
		//备件id
		var codeId = $J("#partsCodeId"+num).val();
			if(codeId == null || codeId == ''){
				MyAlert("请先选备件");
			}else{
				showFactory(id,name,'compensateFactoryCode',codeId,'false');
			}
			
		};
	}
	
	function updatePropertyValueOfFailureLocation(num){
		//故障部位
		var failureLocations = document.getElementsByName("failureLocation");
		var flid = "failureLocation"+num; //故障部位名称
		
		var failureLocationIds = document.getElementsByName("failureLocationId");//故障部位ID的隐藏域
		var fIds = "failureLocationId"+num;//故障部位ID
		
		var failureLocationCodes = document.getElementsByName("failureLocationCode");//故障部位代码的隐藏域
		var fCids = "failureLocationCode"+num;
		
		failureLocations[num].setAttribute("id",flid);
		failureLocationIds[num].setAttribute("id",fIds);
		failureLocationCodes[num].setAttribute("id",fCids);
		var falilurBtns = document.getElementsByName("falilurBtn");
		falilurBtns[num].onclick=function(){
			showFailureParts(fCids,fIds,'false',flid);
		};
	}
	
	
	function updatePropertyValueOfFailureModel(num){
		//故障模式
		var failureModelNames = document.getElementsByName("failureModelName");
		var flid = "failureModelName"+num; //故障模式名称的ID
		
		var failureModelIds = document.getElementsByName("failureModelId");//故障模式ID的隐藏域
		var fIds = "failureModelId"+num;//故障模式ID
		
		var failureModelCodes = document.getElementsByName("failureModelCode");//故障模式代码的隐藏域
		var fCids = "failureModelCode"+num;//故障模式隐藏域ID
		
		failureModelNames[num].setAttribute("id",flid);
		failureModelIds[num].setAttribute("id",fIds);
		failureModelCodes[num].setAttribute("id",fCids);
		var falilurModelBtns = document.getElementsByName("failureModeBtn");
		falilurModelBtns[num].onclick = function(){
			showFaultMode(fIds,fCids,flid,'','false');
		};
		
	}
	
	 function updatePropertyValueOfWorkCode(num){
		//工时代码
		//var modelId = document.getElementById("vehicleId1").value;//车型ID
		var workCodes = document.getElementsByName("workCode");//工时代码
		var flids = "workCode"+num; 
		
		var workNames = document.getElementsByName("workName");//工时代码名称
		var fCs = "workName"+num;
		
		var wNumss = document.getElementsByName("wNums");//工时代码工时数
		var fNums = "wNums"+num;//工时代码工时数的ID
		
		/* var workDateFees = document.getElementsByName("workDateFee");//工时代码工时数
		var fDateFees = "workDateFee"+num;//工时代码工时数的ID */
		
		var workCodeIds = document.getElementsByName("workCodeId");//工时代码ID
		var fWCids = "workCodeId"+num;
		
		workCodes[num].setAttribute("id",flids);
		workNames[num].setAttribute("id",fCs);
		wNumss[num].setAttribute("id",fNums);
		//workDateFees[num].setAttribute("id",fDateFees);
		workCodeIds[num].setAttribute("id",fWCids);
		var wordCodeBtn = document.getElementsByName("wordCodeBtn");
		wordCodeBtn[num].onclick=function(){
			showTask(flids,fCs,fNums,'false',null,null,num,fWCids);
		};
		
	}

	//更新总成分类的行
	 function updateZWRows(num) {
	 	var allHireNameIds = document.getElementsByName("allHireName");//总成
	 	var allHireNameId = "allHireName" + num;
	 	allHireNameIds[num].setAttribute("id", allHireNameId);
	 	
	 	var allHireBtns = document.getElementsByName("allHireBtn");
	 	allHireBtns[num].onclick=function(){
	 		showAssembly(allHireNameId);
	 	};

	 }

	 function updatePropertyValueOfPartsCode(num) {
		//备件编码
		var partsCodeIds = document.getElementsByName("partsCodeId");//备件代码ID
		var flids = "partsCodeId" + num;

		var partsCodes = document.getElementsByName("partsCode");//备件代码名称的ID
		var fCids = "partsCode" + num;

		var partsNames = document.getElementsByName("partsName");//备件代码名称的ID
		var fNames = "partsName" + num;

		/* var partsPrices = document.getElementsByName("partsPrice");//备件代码价格的ID
		var fPrice = "partsPrice" + num; */

		partsCodeIds[num].setAttribute("id", flids);
		partsCodes[num].setAttribute("id", fCids);
		partsNames[num].setAttribute("id", fNames);
		/* partsPrices[num].setAttribute("id", fPrice); */
		var partCodeBtn = document.getElementsByName("partCodeBtn");
		partCodeBtn[num].onclick = function() {
			showPart(flids, fCids, fNames, null, 'false');
		};
	}  
	 
	//备件回调
		function setPartCode(strIds, strs) {
			var tempStrs = strs.split("@@");
	 		var tempStrIds = strIds.split("@@");
			//ID值
			var inputId = tempStrIds[0];
			var inputCode = tempStrIds[1];
			var inputName = tempStrIds[2];
			
			//值
			var id = tempStrs[0];
			var code = tempStrs[1];
			var name = tempStrs[2];
			var priceOfPart = tempStrs[3];
			
			var obj = document.getElementsByName("partsCodeId");
		    for(var i=0;i<obj.length-1;i++){
		        var value = obj[i].value;
		        var ids = obj[i].id;
		        if(ids != inputId){
		        	if(value == id){
			        	alert("备件编码选择重复");
			        	return;
			        }
		        }
		        
		    }
			document.getElementById("inputId").value = inputId;
			document.getElementById("inputCode").value = inputCode;
			document.getElementById("inputName").value = inputName;
			document.getElementById("inputId1").value = id;
			document.getElementById("inputCode1").value = code;
			document.getElementById("inputName1").value = name;
			//三包内的都要执行此操作备件的价格要乘以浮动率
			//计算备件的价格的浮动率
			 sendAjax(globalContextPath + "/AfterSales/threeGuarantees/AfterSalesThreeGuaranAction/floatPriceQuery.json?priceOfPart=" + priceOfPart + "&partCodeId=" + id,dealWithPartPrice,"fm");
		}
	
	
		function dealWithPartPrice(json) {
			//var zcfl = json.zcfl;//总成分类
			//id值
			var inputId = document.getElementById("inputId").value;
			var inputCode = document.getElementById("inputCode").value;
			var inputName = document.getElementById("inputName").value;
			//值
			document.getElementById(inputId).value = document.getElementById("inputId1").value;
			document.getElementById(inputCode).value = document.getElementById("inputCode1").value;
			document.getElementById(inputName).value = document.getElementById("inputName1").value;
			//截取字符串partsCodeId+num
		/*	var num = inputId.substring(11); 
			var allHireName = "allHireName" + num;
			document.getElementById(allHireName).value = zcfl; */
		}
	
	function delRows(obj) {
		var trElement = obj.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
		obj.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.removeChild(trElement);
		var orderNums = document.getElementsByClassName("orderNum");
		var allWDateFee = 0;
		for (var i = 0; i < orderNums.length-1; i++) {
			orderNums[i].innerHTML = i+1;//重新计算行号
		}
	}
	
	
	
	

