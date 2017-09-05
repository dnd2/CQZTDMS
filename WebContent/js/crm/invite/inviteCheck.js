	var context=null;
	var myPage;
	var url = null;
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex},
				{header: "顾问", dataIndex: 'NAME', align:'center'},
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "意向车型", dataIndex: 'SERIES_NAME', align:'center'},
				{header: "意向等级", dataIndex: 'OLD_LEVEL', align:'center',renderer:getItemValue},
				{header: "计划邀约时间", dataIndex: 'PLAN_INVITE_DATE', align:'center'},
				{header: "计划见面时间", dataIndex: 'PLAN_MEET_DATE', align:'center'},
				{header: "操作", dataIndex: 'INVITE_ID', align:'center',renderer:inviteCheckInit}
		      ];
	function doInit(){
		context=$('curPaths').value;
		url=context+"/crm/invite/InviteCheck/inviteCheckQueryList.json?COMMAND=1"
		__extQuery__(1);
	} 
	function inviteCheckInit(value,meta,record){
		var invite_id=record.data.INVITE_ID;
		var str="";
		str+="<a href='#' onclick='inviteCheck(\""+ invite_id +"\")'>[审核]</a>";
		return String.format(str) ;
	}
	function inviteCheck(inviteId){
		var urls = context+"/crm/invite/InviteCheck/checkInit.do?inviteId="+inviteId;
		location.href=urls;
	}
	//点击保存时候执行的方法
	function auditSubmit(){
		var planMeetDate = document.getElementById("planMeetDate").value;
		var planInviteDate = document.getElementById("planInviteDate").value;
		var dd = new Date();
		var planIDate=parseDate(planInviteDate);
		var planMDate=parseDate(planMeetDate);
		if(planInviteDate==null || planInviteDate=="") {
			alert("请选择计划邀约时间");
			return false;
		}
		
		if(planMeetDate==null||planMeetDate=="") {
			alert("请选择计划见面时间！");
			return false;
		}
		
		if(planIDate < dd.setDate(dd.getDate()-1)){
			alert("邀约时间要大于当前时间！");
			return false;
		}
		if(planMDate < planIDate){
			alert("计划见面时间大于邀约时间！");
			return false;
		}
		var urls=context+"/crm/invite/InviteCheck/inviteCheck.json";
		if(!confirm("是否确认操作")){
			return false;
		};
		document.getElementById("addSub").disabled = true;
		makeFormCall(urls, auditResult, "fm") ;
	}
	function auditResult(json){
		if(json.flag==1){
			alert("操作成功！！！");
			location.href=context+"/crm/invite/InviteCheck/doInit.do";
		}else{
			alert("操作失败！！！");
			document.getElementById("addSub").disabled = false;
		}
	}
	function parseDate(str)  
	{
	    return new Date(Date.parse(str.replace(/-/g,"/")));
	}
	