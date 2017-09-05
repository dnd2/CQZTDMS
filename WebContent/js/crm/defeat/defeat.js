	var context=null;
	var myPage;
	var url = null;
	var title = null;
	//规定页面要展示的字典
	var columns = [
				{header: "顾问", dataIndex: 'ADVISER', align:'center'},
				{header: "类型", align: 'center',dataIndex:'IS_DIRECT_DEFEAT', renderer:getItemValueByStatus},
				{header: "客户名称", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "意向车型", dataIndex: 'INTENT_VEHICLE', align:'center'},
				{header: "战败车型", dataIndex: 'DEFEAT_MODEL', align:'center'},
//				{header: "生命周期", dataIndex: 'LIFE_CYCLE', align:'center'},
				{header: "战败时间", dataIndex: 'DEFEAT_END_DATE', align:'center'},
//				{header: "实效时间", dataIndex: 'FAILURE_DATE', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center'},
				{header: "意向等级", dataIndex: 'CTM_RANK', align:'center'},
				{header: "销售流程进度", dataIndex: 'SALES_PROGRESS', align:'center'},
				{header: "操作", dataIndex: 'GROUP_ID', align:'center',renderer:defeatUpdateInit}
		      ];
	//进入战败实效查询页面执行的方法
	function doInit(){
		url=globalContextPath+"/crm/defeat/DefeatManage/defeatQueryList.json?COMMAND=1"
		__extQuery__(1);
	} 
	
	function getItemValueByStatus(value,meta,record){
		var defeatStatus = record.data.IS_DIRECT_DEFEAT;
		if (defeatStatus == "10011002") {
			return "直接战败";
		} else {
			return "跟进战败";
		}
	}
	
	//操作中有的功能
	function defeatUpdateInit(value,meta,record){
		var defeat_id=record.data.DEFEATFAILURE_ID;
		var audit_status=record.data.AUDIT_STATUS;
		var ctmId=record.data.CUSTOMER_ID;
		var pose_rank=record.data.POSE_RANK;
		var defeatStatus = record.data.IS_DIRECT_DEFEAT;
		var status = record.data.LEADS_STATUS;
		var str="";
		if(audit_status=='60401001'||audit_status=='60401005'){
			if(pose_rank=="60281003"||pose_rank=='60281002'){
				if(audit_status=='60401005'){
					str+="<a href='#' onclick='defeatAudit(\""+ defeat_id +"\")'>[重新分派]/</a>";
				}else{
					if (status == "60161002") {
						if (defeatStatus == "10011002") {
							str+="<a href='#' onclick='defeatAuditRedirect(\""+ defeat_id +"\")'>[审核]/</a>";
						} else {
							str+="<a href='#' onclick='defeatAudit(\""+ defeat_id +"\")'>[审核]/</a>";
						}
					}
				}
			}
			//直接战败跳转的页面不同
			if (defeatStatus == "10011002") {
				str+="<a href='#' onclick='defeatDetailByRedirect(\""+ defeat_id +"\")'>[查看]</a>"
			} else {
				str+="<a href='#' onclick='defeatDetail(\""+ defeat_id +"\")'>[查看]</a>"
			}
		}else{
			if (defeatStatus == "10011002") {
				str+="<a href='#' onclick='defeatDetailByRedirect(\""+ defeat_id +"\")'>[查看]</a>"
			} else {
				str+="<a href='#' onclick='defeatDetail(\""+ defeat_id +"\")'>[查看]</a>"
			}
		}
		/*if(audit_status=='60401001'||audit_status=='60401005'){
			if(pose_rank=="60281003"||pose_rank=='60281002'){
				if(audit_status=='60401005'){
					str+="<a href='#' onclick='defeatAudit(\""+ defeat_id +"\")'>[重新分派]/</a>";
				}else{
					str+="<a href='#' onclick='defeatAudit(\""+ defeat_id +"\")'>[审核]/</a>";
				}
			}
			str+="<a href='#' onclick='defeatDetail(\""+ defeat_id +"\")'>[查看]</a>"
		}else{
			str+="<a href='#' onclick='defeatDetail(\""+ defeat_id +"\")'>[查看]</a>"
		}*/
		//}else{
		//	str+="<a href='#' onclick='toDepartAdviser(\""+ ctmId +"\")'>[重新分配]</a>";
		//}
		
		return String.format(str) ;
	}
	//点击审核时执行的方即页面跳转
	function defeatAudit(defeat_id){
		var urls = globalContextPath+"/crm/defeat/DefeatManage/detailInit.do?defeatId="+defeat_id;
		location.href=urls;
	}
	//点击审核时执行的方即页面跳转（直接战败）
	function defeatAuditRedirect(defeat_id){
		var urls = globalContextPath+"/crm/defeat/DefeatManage/detailInitRedirect.do?defeatId="+defeat_id;
		location.href=urls;
	}
	//点击查看时执行的方即页面跳转
	function defeatDetail(defeat_id){
		var urls = globalContextPath+"/crm/defeat/DefeatManage/defeatDetailInit.do?defeatId="+defeat_id;
		location.href=urls;
	}
	//点击查看时执行的方即页面跳转(直接战败的)
	function defeatDetailByRedirect(defeat_id){
		var urls = globalContextPath+"/crm/defeat/DefeatManage/defeatDetailRedirect.do?defeatId="+defeat_id;
		location.href=urls;
	}
	//点击审核通过时执行的方法
	function auditPass(){
		var defeat_id=document.getElementById("defeatId").value;
		var urls = globalContextPath+"/crm/defeat/DefeatManage/defeatAudit.json?defeatId="+defeat_id+"&status="+60401002;
		document.getElementById("auditSub").disabled=true;
		makeFormCall(urls, auditResult, "fm") ;
	
	}
	function auditResult(json){
		if(json.flag=='1'){
			alert("操作成功！！！");
			var urls = globalContextPath+"/crm/defeat/DefeatManage/doInit.do";
			location.href=urls;
		}else{
			alert("操作失败！！！");
		}
	}
	//点击审核驳回时执行的方法
	function auditBack(){
		if($("backDiv").style.display=="block"){
			$("backDiv").style.display="none";
		}else{
			$("backDiv").style.display="block";
		}
		$("groupRadio").style.display="block";
	}
	
	//修改车辆信息
	function toDepartAdviser(ctmId){
		OpenHtmlWindow(globalContextPath+'/crm/defeat/DefeatManage/toAdviserInit.do?ctmId='+ctmId,400,300);
	}
	//客户等级下拉列表选择时执行的方法
	function loadCtmRank(obj){
		$("ctmRank").value=obj.getAttribute("TREE_ID");
	}
	//销售流程进度选择时执行的下拉列表
	function loadProgress(obj){
		$("salesProgress").value=obj.getAttribute("TREE_ID");
	}
	//销售流程进度选择时执行的下拉列表
	function loadType(obj){
		$("opType").value=obj.getAttribute("TREE_ID");
	}
	
	//选择跟进执行的js
	function followClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		followTable.style.display = "block";
		inviteTable.style.display = "none";
		var sureTable = document.getElementById("sureTable");
		sureTable.style.display="block";
	}
	//选择邀约执行的js
	function inviteClick(){
		var followTable = document.getElementById("follow_table");
		var inviteTable = document.getElementById("invite_table");
		followTable.style.display = "none";
		inviteTable.style.display = "table-row";
		var sureTable = document.getElementById("sureTable");
		sureTable.style.display="table-row";
	}
	//选择邀约计划时执行的js
	function checkClick(){
		var check = document.getElementById("checkbox");
		var yaoyuejihua = document.getElementById("yaoyuejihua");
		var yaoyuejihua2 = document.getElementById("yaoyuejihua2");
		if(check.checked){
			yaoyuejihua.style.display = "block";
			yaoyuejihua2.style.display = "block";
		} else {
			yaoyuejihua.style.display = "none";
			yaoyuejihua2.style.display = "none";
		}
	}
	
	//驳回点击确认执行的方法
	function sureSubmit(){
		var follow_radio=$("follow_radio").checked;
		var context=$("curPaths").value;
		var defeat_id=$("defeatId").value;
		var urls=null;
		var inbound=document.getElementById("ctm_rank1").value;
		if(follow_radio){
			var dd = new Date();
			var followType=document.getElementById("follow_type").value;
			var nextFollowDate = document.getElementById("next_follow_date").value;
			var nextDate=parseDate(nextFollowDate);
			if(inbound == "A"){
				var da = new Date();
		    	da.setDate(da.getDate()+6);
				if(nextDate > da ){
					alert("A等级跟进时间要小于7天");
					return false;
				}	
			}
			if(inbound == "B"){
				var db = new Date();
		    	db.setDate(db.getDate()+14);
				if(nextDate >  db ){
					alert("B等级跟进时间要小于15天");
					return false;
				}
			}
			if(inbound == "C"){
				var dc = new Date();
		    	dc.setDate(dc.getDate()+29);
				if(nextDate > dc ){
					alert("C等级跟进时间要小于30天");
					return false;
				}	
			}
			 if(inbound == "H"){
				    var dh = new Date();
			    	dh.setDate(dh.getDate()+2);
					if(nextDate > dh){
						alert("H等级跟进时间要小于3天");
						return false;
					}	
			} 
			 	if(nextFollowDate==null||nextFollowDate=="") {
					alert("请选择下次跟进时间！");
					return false;
				} else if(followType==null||followType=="") {
					alert("请选择跟进方式！");
					return false;
				}else if(nextDate <= dd.setDate(dd.getDate()-1)){
					alert("跟进时间要大于当前时间");
					return false;
				}
			 	if(inbound == "O"){
			 		alert("跟进时意向等级不能选择O级");
					return false;
			 	}else if(inbound == "E"){
			 		alert("跟进时意向等级不能选择E级");
					return false;
			 	}else if(inbound == "L"){
			 		alert("跟进时意向等级不能选择L级");
					return false;
			 	}
			urls=globalContextPath+"/crm/defeat/DefeatManage/defeatFollow.json?defeatId="+defeat_id+"&status="+60401003;
		}else{
			var planInviteDate = document.getElementById("plan_invite_date").value;
			var planMeetDate = document.getElementById("plan_meet_date").value;
			var dd = new Date();
			var planIDate=parseDate(planInviteDate);
			var planMDate=parseDate(planMeetDate);
			var inviteTypeNew = document.getElementById("invite_type_new").value;
			if(inbound == "A"){
				var da = new Date();
		    	da.setDate(da.getDate()+6);
				if(planIDate > da ){
					alert("A等级邀约时间要小于7天");
					return false;
				}	
			}
			if(inbound == "B"){
				var db = new Date();
		    	db.setDate(db.getDate()+14);
				if(planIDate >  db ){
					alert("B等级邀约时间要小于15天");
					return false;
				}
			}
			if(inbound == "C"){
				var dc = new Date();
		    	dc.setDate(dc.getDate()+29);
				if(planIDate > dc ){
					alert("C等级邀约时间要小于30天");
					return false;
				}	
			}
			 if(inbound == "H"){
				    var dh = new Date();
			    	dh.setDate(dh.getDate()+2);
					if(planIDate > dh){
						alert("H等级邀约时间要小于3天");
						return false;
					}	
			}
			
			if(planInviteDate==null||planInviteDate=="") {
				alert("请选择计划邀约时间");
				return false;
			}
			if(planMeetDate==null||planMeetDate=="") {
				alert("请选择计划见面时间！");
				return false;
			}
			if(inviteTypeNew==null||inviteTypeNew=="") {
				alert("请选择邀约方式！");
				return false;
			}
			if(planIDate <= dd.setDate(dd.getDate()-1)){
				alert("邀约时间要大于当前时间！");
				return false;
			}
			if(planMDate <planIDate){
				alert("计划见面时间大于邀约时间！");
				return false;
			}
			if(inbound == "O"){
		 		alert("邀约时意向等级不能选择O级");
				return false;
		 	}else if(inbound == "E"){
		 		alert("邀约时意向等级不能选择E级");
				return false;
		 	}else if(inbound == "L"){
		 		alert("邀约时意向等级不能选择L级");
				return false;
		 	}
			urls=globalContextPath+"/crm/defeat/DefeatManage/defeatInvite.json?defeatId="+defeat_id+"&status="+60401003;
		}
		// urls= context+"/crm/defeat/DefeatManage/defeatAudit.json?defeatId="+defeat_id+"&status="+60401003;
		document.getElementById("sure").disabled=true;
		makeFormCall(urls, backResult, "fm") ;
	}
	//驳回确认回调函数
	function backResult(json){
		if(json.flag=='1'){
			alert("操作成功！！！");
			var urls = globalContextPath+"/crm/defeat/DefeatManage/doInit.do";
			location.href=urls;
		}else{
			alert("操作失败！！！");
		}
	}
	function changeSelect(obj){
		var curData="";
		for(var i=0;i<obj.options.length;i++){
			if(obj.options[i].selected){
				curData=obj.options[i].innerHTML;
			}
		}
		$("ctm_rank1").value=curData;
	}

	var dd1 = new Date(); 
	var data= dd1.Format("yyyy-M-d");
	function changeEvent(){
		var inbound=document.getElementById("ctm_rank1").value;
		var nextDate=document.getElementById("next_follow_date2");
		if(inbound == "A"){
			var dd = new Date();
		    	dd.setDate(dd.getDate()+6);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "B"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+14);
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "C"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+29);
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		 if(inbound == "H"){
			 var dd = new Date();
		    	dd.setDate(dd.getDate()+2);
			nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data,dd.Format("yyyy-M-d")));
		}
		nextDate.addEventListener("click",showcalendar(event, 'next_follow_date', false,data));
	}
	function changeEvent1(){
		var inbound=document.getElementById("ctm_rank1").value;
		var nextDate=document.getElementById("plan_invite_date2");
		if(inbound == "A"){
			var dd = new Date();
		    	dd.setDate(dd.getDate()+6);
			nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "B"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+14);
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		if(inbound == "C"){
			var dd = new Date();
	    	dd.setDate(dd.getDate()+29);
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		 if(inbound == "H"){
			 var dd = new Date();
		    	dd.setDate(dd.getDate()+2);
			nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data,dd.Format("yyyy-M-d")));
		}
		nextDate.addEventListener("click",showcalendar(event, 'plan_invite_date', false,data));
	}
	function parseDate(str)  
	{
	    return new Date(Date.parse(str.replace(/-/g,"/")));
	}