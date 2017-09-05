	var context=null;
	var myPage;
	var url = null;
	var title = null;
	var columns = [
				{header: "顾问", dataIndex: 'NAME',align:'center'},
				{header: "客户姓名", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "客户等级", dataIndex: 'CTM_RANK', align:'center',renderer:getItemValue},
				{header: "邀约方式", dataIndex: 'INVITE_TYPE', align:'center',renderer:getItemValue},
				{header: "邀约类型", dataIndex: 'INVITE_WAY', align:'center',renderer:getItemValue},
				{header: "是否邀约成功", dataIndex: 'IF_INVITE', align:'center',renderer:getItemValue},
				{header: "邀约时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "操作", dataIndex: 'INVITE_ID', align:'center',renderer:inviteDetailInit}
		      ];
	function doInit(){
		context=$('curPaths').value
		url=context+"/crm/invite/InviteManage/inviteQueryList.json?COMMAND=1"
		__extQuery__(1);
	} 
	//操作后面执行的方法
	function inviteDetailInit(value,meta,record){
		var invite_id=record.data.INVITE_ID;
		var audit_status=record.data.DIRECTOR_AUDIT;
		var str="";
		//审核驳回的显示修改
		if(audit_status=="60191003"){
			str+="<a href='#' onclick='inviteDetail(\""+ invite_id +"\")'>[详情]</a>/<a href='#' onclick='inviteUpdate(\""+ invite_id +"\")'>[邀约计划修改]</a>";
		}else{
			str+="<a href='#' onclick='inviteDetail(\""+ invite_id +"\")'>[详情]</a>";
		}
		return String.format(str) ;
	}
	//点击详情执行的方法
	function inviteDetail(invite_id){
		var urls = context+"/crm/invite/InviteManage/inviteDetailInit.do?inviteId="+invite_id;
		location.href=urls;
	}
	//点击修改执行的方法
	function inviteUpdate(invite_id){
		var urls = context+"/crm/invite/InviteManage/doUpdateInit.do?inviteId="+invite_id;
		location.href=urls;
	}
	//修改页面点击保存执行的方法
	function updateSubmit(){
		var invite_id=$("invite_id").value;
		var urls=context+"/crm/invite/InviteManage/doUpdate.json?inviteId="+invite_id;
		makeFormCall(urls, updateResult, "fm") ;
	}
	//修改完成后的回调函数
	function updateResult(json){
		if(json.flag==1){
			alert("修改成功！！");
			location.href=context+"/crm/invite/InviteManage/doInit.do";
			
		}else{
			alert("修改失败！！");
		}
	}
	function loadInviteType(obj){
		$("inviteType").value=obj.getAttribute("TREE_ID");
	}
	function loadCtmRank(obj){
		$("ctmRank").value=obj.getAttribute("TREE_ID");
	}
