	var context=null;
	var myPage;
	var url = null;
	var title = null;
	var columns = [
				//{header: "序号", align:'center', renderer:getIndex},
				{header: "上级经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "上级经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "集团名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作", dataIndex: 'GROUP_ID', align:'center',renderer:groupUpdateInit}
		      ];
	function doInit(){
		context=$('curPaths').value
		url=context+"/crm/companygroup/CompanyGroup/groupQueryList.json?COMMAND=1"
		__extQuery__(1);
	} 
	function groupUpdateInit(value,meta,record){
		var groupId=record.data.GROUP_ID;
		var str="";
		str+="<a href='#' onclick='groupUpdate(\""+ groupId +"\")'>[修改]</a>";
		return String.format(str) ;
	}
	function groupUpdate(groupId){
		var urls = context+"/crm/companygroup/CompanyGroup/groupUpdateInit.do?groupId="+groupId;
		location.href=urls;
	}
	
	function updateSubmit(){
		var status=$("status").value;
		if(status==null||''==status){
			alert("请选择用户组状态！！！");
			return;
		}
		checkName($("groupName").value,$("companyGroupId").value);
	}
	function updateResult(json){
		if(json.flag==1){
			alert("修改成功！！");
			location.href=context+"/crm/companygroup/CompanyGroup/doInit.do";
			
		}else{
			alert("修改失败！！");
		}
	}
	function addGroup(){
		location.href=context+"/crm/companygroup/CompanyGroup/groupAddInit.do";
	}
	//新增保存
	function addSubmit(){
		checkGroupName($("groupName").value);
	}
	
	//下拉列表选择时执行的方法
	function loadStatus(obj) {
		document.getElementById("status").value = obj.getAttribute("TREE_ID");
	}
	//验证组名称
	function checkGroupName(groupName){
			var urls=context+"/crm/companygroup/CompanyGroup/checkGroupName.json";
			makeCall(urls, checkGroupResult, {groupName:groupName}) ;
	}
	//验证组名称结果
	function checkGroupResult(json){
		if(json.flag=="1"){
			if(submitForm("fm")){
				var urls=context+"/crm/companygroup/CompanyGroup/groupAdd.json";
				makeFormCall(urls, addGroupResult, "fm") ;
			}
		}else{
			alert("组名称重复");
		}
	}
	
	function addGroupResult(json){
		if(json.flag==1){
			alert("新增成功！！");
			location.href=context+"/crm/companygroup/CompanyGroup/doInit.do";
			
		}else{
			alert("新增失败！！");
		}
	}
	//修改时验证组名称
	function checkName(groupName,groupId){
			var urls=context+"/crm/companygroup/CompanyGroup/checkGroupName.json";
			makeCall(urls, checkNameResult, {groupName:groupName,groupId:groupId}) ;
	}
	//验证组名称结果
	function checkNameResult(json){
		if(json.flag=="1"){
			if(submitForm("fm")){
				var urls=context+"/crm/companygroup/CompanyGroup/groupUpdate.json";
				makeFormCall(urls, updateResult, "fm") ;
			}
		}else{
			alert("组名称重复");
		}
	}
	
	
	