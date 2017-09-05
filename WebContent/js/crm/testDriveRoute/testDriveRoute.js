	var context=null;
	var myPage;
	var url = null;
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex},
				{header: "路线名称", dataIndex: 'ROUTE_NAME', align:'center'},
				{header: "开始地点", dataIndex: 'START_LINE', align:'center'},
				{header: "结束地点", dataIndex: 'END_LINE', align:'center'},
				{header: "试驾里程数", dataIndex: 'MILEAGE', align:'center'},
				{header: "操作", dataIndex: 'ROUTE_ID', align:'center',renderer:routeUpdateInit}
		      ];

	function doInit(){
		context=$('curPaths').value;
		url=context+"/crm/testDriveRoute/TestDriveRoute/routeQueryList.json?COMMAND=1";
		__extQuery__(1);
	} 
	function routeUpdateInit(value,meta,record){
		var route_id=record.data.ROUTE_ID;
		var str="";
		str+="<a href='#' onclick='routeUpdate(\""+ route_id +"\")'>[修改]</a>";
		return String.format(str) ;
	}
	function routeUpdate(routeId){
		var urls = context+"/crm/testDriveRoute/TestDriveRoute/routeUpdateInit.do?routeId="+routeId;
		location.href=urls;
	}
	
	function updateSubmit(){
		alert('===');
	 if(submitForm("fm")){
		var urls=context+"/crm/testDriveRoute/TestDriveRoute/routeUpdate.json";
		makeFormCall(urls, updateResult, "fm") ;
	  }
	}
	function updateResult(json){
		if(json.flag==1){
			alert("修改成功！！");
			location.href=context+"/crm/testDriveRoute/TestDriveRoute/doInit.do";
			
		}else{
			alert("修改失败！！");
		}
	}
	function addRoute(){
		location.href=context+"/crm/testDriveRoute/TestDriveRoute/routeAddInit.do";
	}
	
	function addSubmit(){
		if(submitForm("fm")){
			var urls=context+"/crm/testDriveRoute/TestDriveRoute/routeAdd.json";
			makeFormCall(urls, addResult, "fm") ;
		}
		
	}
	function addResult(json){
		if(json.flag==1){
			alert("新增成功！！");
			location.href=context+"/crm/testDriveRoute/TestDriveRoute/doInit.do";
			
		}else{
			alert("新增失败！！");
		}
	}
	//下拉列表选择时执行的方法
	function loadStatus(obj) {
		document.getElementById("status").value = obj.getAttribute("TREE_ID");
	}

	