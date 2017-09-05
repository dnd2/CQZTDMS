	var context=null;
	var myPage;
	var url = null;
	var title = null;
	//规定页面要展示的字典
	var columns = [
				{header: "顾问", dataIndex: 'ADVISER', align:'center'},
				{header: "类型", align: 'center',dataIndex:'DEFEATFAILURE_TYPE', renderer:getItemValue},
				{header: "客户名称", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "意向车型", dataIndex: 'INTENT_VEHICLE', align:'center'},
				{header: "战败车型", dataIndex: 'DEFEAT_MODEL', align:'center'},
//				{header: "生命周期", dataIndex: 'LIFE_CYCLE', align:'center'},
				{header: "战败时间", dataIndex: 'DEFEAT_END_DATE', align:'center'},
				{header: "实效时间", dataIndex: 'FAILURE_DATE', align:'center'},
				{header: "意向等级", dataIndex: 'CTM_RANK', align:'center'},
				{header: "销售流程进度", dataIndex: 'SALES_PROGRESS', align:'center'},
				{header: "操作", dataIndex: 'GROUP_ID', align:'center',renderer:defeatUpdateInit}
		      ];
	//进入战败实效查询页面执行的方法
	function doInit(){
		context=$('curPaths').value;
		url=context+"/crm/defeat/DefeatManage/dcrcQueryList.json?COMMAND=1"
		__extQuery__(1);
	} 
	//操作中有的功能
	function defeatUpdateInit(value,meta,record){
		var defeat_id=record.data.DEFEATFAILURE_ID;
		var str="";
		str+="<a href='#' onclick='defeatAudit(\""+ defeat_id +"\")'>[抽查]</a>";
		return String.format(str) ;
	}
	//点击抽查时执行 即页面跳转
	function defeatAudit(defeat_id){
		var urls = context+"/crm/defeat/DefeatManage/detailCheckInit.do?defeatId="+defeat_id;
		location.href=urls;
	}
	//点击审核通过时执行的方法
	function auditPass(){
	document.getElementById("auditBtn").disabled=true;
		var context=$("curPaths").value;
		var defeat_id=$("defeatId").value;
		var urls = context+"/crm/defeat/DefeatManage/dcrcAudit.json?defeatId="+defeat_id+"&status="+60401004;
		makeFormCall(urls, auditResult, "fm") ;
	
	}
	//点击审核驳回时执行的方法
	function auditResult(json){
		if(json.flag=='1'){
			alert("操作成功！！！");
			var urls = context+"/crm/defeat/DefeatManage/doCheckInit.do";
			location.href=urls;
		}else{
			alert("操作失败！！！");
		}
	}
	//点击审核驳回时执行的方法
	function auditBack(){
		document.getElementById("returnBtn").disabled=true;
		var context=$("curPaths").value;
		var defeat_id=$("defeatId").value;
		var urls = context+"/crm/defeat/DefeatManage/dcrcAudit.json?defeatId="+defeat_id+"&status="+60401005;
		makeFormCall(urls, auditResult, "fm") ;
	
	}
	//客户等级下拉列表选择时执行的方法
	function loadCtmRank(obj){
		$("ctmRank").value=obj.getAttribute("TREE_ID");
	}
	//销售流程进度选择时执行的下拉列表
	function loadProgress(obj){
		$("salesProgress").value=obj.getAttribute("TREE_ID");
	}
	//选择类型
	function loadType(obj){
		$("opType").value=obj.getAttribute("TREE_ID");
	}