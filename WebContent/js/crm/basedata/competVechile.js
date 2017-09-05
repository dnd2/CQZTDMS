	var context=null;
	var myPage;
	var url = null;
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex},
				{header: "竞品等级", dataIndex: 'COMPET_LEVEL', align:'center'},
				{header: "竞品代码", dataIndex: 'COMPET_CODE', align:'center'},
				{header: "竞品名称", dataIndex: 'COMPET_NAME', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作", dataIndex: 'COMPET_ID', align:'center',renderer:groupUpdateInit}
		      ];
	function doInit(){
		context=$('curPaths').value
		url=context+"/crm/basedata/PcCompetVechile/vechileQueryList.json?COMMAND=1"
		__extQuery__(1);
	} 
	function groupUpdateInit(value,meta,record){
		var compet_id=record.data.COMPET_ID;
		var str="";
		str+="<a href='#' onclick='groupUpdate(\""+ compet_id +"\")'>[修改]</a>";
		return String.format(str) ;
	}
	function groupUpdate(compet_id){
		var urls = context+"/crm/basedata/PcCompetVechile/vechileUpdateInit.do?competId="+compet_id;
		location.href=urls;
	}
	
	function updateSubmit(){
	 if(submitForm("fm")){
		var urls=context+"/crm/basedata/PcCompetVechile/vechileUpdate.json";
		makeFormCall(urls, updateResult, "fm") ;
	  }
	}
	function updateResult(json){
		if(json.flag==1){
			alert("修改成功！！");
			location.href=context+"/crm/basedata/PcCompetVechile/doInit.do";
			
		}else{
			alert("修改失败！！");
		}
	}
	function addGroup(){
		location.href=context+"/crm/basedata/PcCompetVechile/vechileAddInit.do";
	}
	
	function addSubmit(){
		if(submitForm("fm")){
			var urls=context+"/crm/basedata/PcCompetVechile/vechileAdd.json";
			makeFormCall(urls, addResult, "fm") ;
		}
		
	}
	function addResult(json){
		if(json.flag==1){
			alert("新增成功！！");
			location.href=context+"/crm/basedata/PcCompetVechile/doInit.do";
			
		}else{
			alert("新增失败！！");
		}
	}
	
	
	//意向车型
	function toCompetVechileList(level){
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/basedata/PcCompetVechile/toCompetVechileList.do?level='+level,800,600);
	}
	 //显示意向车型
	function showCompetInfo(compet_id,compet_name){
			document.getElementById("par_name").value = compet_name;
			document.getElementById("parId").value = compet_id;
		
	}
	
	function clrCompetTxt(){
			document.getElementById("par_name").value = "";
			document.getElementById("parId").value = "";
		
	}
	
	