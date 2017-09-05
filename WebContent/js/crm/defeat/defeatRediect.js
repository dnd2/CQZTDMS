	var context=null;
	var myPage;
	var url = null;
	var title = null;
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
	function auditPassRedirect(){
		var context=document.getElementById("curPaths").value;
		var defeat_id=document.getElementById("defeatId").value;
		var urls = globalContextPath+"/crm/defeat/DefeatManage/defeatAuditRedirect.json?defeatId="+defeat_id+"&status="+60401002;
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
		inviteTable.style.display = "inline";
		var sureTable = document.getElementById("sureTable");
		sureTable.style.display="inline";
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
			urls=globalContextPath+"/crm/defeat/DefeatManage/defeatFollowRedirect.json?defeatId="+defeat_id+"&status="+60401003;
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