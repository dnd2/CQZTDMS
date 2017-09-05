
	var context=null;
	var myPage;
	var url = null;
	var title = null;
	var columns = null;
		      
	function setCols(){
		columns=[
				{header: "操作",dataIndex:'CUSTOMER_ID',align:'center',renderer:linkCustomer},
				{header: "顾问名称", dataIndex:'NAME',  align:'center'},
				{header: "客户名称", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "客户电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "客户等级", dataIndex: 'CTM_RANK', align:'center',renderer:getItemValue},
				{header: "客户类型", dataIndex: 'CTM_TYPE', align:'center',renderer:getItemValue},
				{header: "集客方式", dataIndex: 'JC_WAY', align:'center'},
				{header: "意向车型", dataIndex: 'INTENT_VEHICLE', align:'center'},
				{header: "操作", dataIndex: 'CUSTOMER_ID', align:'center',renderer:ctmUpdateInit}
		      ];
		context=$('curPaths').value;
		url=context+"/crm/customer/CustomerManage/customerQueryList.json?COMMAND=1";
		var isMgr=$("isMgr").value;
		if(isMgr=="0"){
			setCols2();
			__extQuery__(1);
		}
	}	
	function setCols1(){
		columns=[
				{header: "操作",dataIndex:'CUSTOMER_ID',align:'center',renderer:linkCustomer},
				{header: "顾问名称", dataIndex:"NAME",align:'center'},
				{header: "客户名称", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "客户电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "客户等级", dataIndex: 'CTM_RANK', align:'center',renderer:getItemValue},
				{header: "客户类型", dataIndex: 'CTM_TYPE', align:'center',renderer:getItemValue},
				{header: "集客方式", dataIndex: 'JC_WAY', align:'center'},
				{header: "意向车型", dataIndex: 'INTENT_VEHICLE', align:'center'},
				{header: "激活状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作", dataIndex: 'CUSTOMER_ID', align:'center',renderer:ctmUpdateInit}
		      ];
		context=$('curPaths').value;
		url=context+"/crm/customer/CustomerManage/customerAllList.json?COMMAND=1";
		var isMgr=$("isMgr").value;
		if(isMgr=="0"){
			setCols3();
			__extNewQuery__(1);
		}
	}	 
	
	function setCols2(){
		columns=[
				{header: "顾问名称", dataIndex:'NAME',  align:'center'},
				{header: "客户名称", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "客户电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "客户等级", dataIndex: 'CTM_RANK', align:'center',renderer:getItemValue},
				{header: "客户类型", dataIndex: 'CTM_TYPE', align:'center',renderer:getItemValue},
				{header: "集客方式", dataIndex: 'JC_WAY', align:'center'},
				{header: "意向车型", dataIndex: 'INTENT_VEHICLE', align:'center'},
				{header: "操作", dataIndex: 'CUSTOMER_ID', align:'center',renderer:ctmUpdateInit}
		      ];
		context=$('curPaths').value;
		url=context+"/crm/customer/CustomerManage/customerQueryList.json?COMMAND=1";
	}
	
	function setCols3(){
		columns=[
				{header: "顾问名称", dataIndex:'NAME',  align:'center'},
				{header: "客户名称", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "客户电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "客户等级", dataIndex: 'CTM_RANK', align:'center',renderer:getItemValue},
				{header: "客户类型", dataIndex: 'CTM_TYPE', align:'center',renderer:getItemValue},
				{header: "集客方式", dataIndex: 'JC_WAY', align:'center'},
				{header: "意向车型", dataIndex: 'INTENT_VEHICLE', align:'center'},
				{header: "操作", dataIndex: 'CUSTOMER_ID', align:'center',renderer:ctmUpdateInit}
		      ];
		context=$('curPaths').value;
		url=context+"/crm/customer/CustomerManage/customerAllList.json?COMMAND=1";
	}
	
	function doInit(){
		context=$('curPaths').value;
		document.getElementById("query").click();
		document.getElementById("queryBtn").click();
		
	} 
	
	function ctmUpdateInit(value,meta,record){
		var ctmId=record.data.CUSTOMER_ID;
		var str="";
		str+="<a href='#' onclick='ctmUpdate(\""+ ctmId +"\")'>[查看]</a>";
		return String.format(str) ;
	}
	//点击查看执行的方法
	function ctmUpdate(ctmId){
		var urls = context+"/crm/customer/CustomerManage/ctmUpdateInit.do?ctmId="+ctmId+"&isClose=1";
		location.href=urls;
	}
	//修改页面点击保存时执行的方法
	function updateSubmit()
	{
		var telephone = document.getElementById("telephone").value;//获取手机号码数据
		 
	    if(telephone.length  != 11)
	    {
	        alert('请输入有效的手机号码！');
	        return false;
	    }
	 	if(!submitForm("fm")){
		 	return;
		 }
		var ctmId=$("ctmId").value;
		var urls=context+"/crm/customer/CustomerManage/customerUpdate.json?ctmId="+ctmId;
		makeFormCall(urls, updateResult, "fm") ;
	}
	 function validatemobile(mobile)
	    {
	        if(mobile.length==0)
	        {
	           alert('请输入手机号码！');
	           return false;
	        }    
	        if(mobile.length!=11)
	        {
	            alert('请输入有效的手机号码！');
	            return false;
	        }
	        
	        var myreg = /^(((13[0-9]{1})|159|153)+\d{8})$/;
	        if(!myreg.test(mobile))
	        {
	            alert('请输入有效的手机号码！');
	            return false;
	        }
	        return true;
	    }
	function updateResult(json){
		if(json.flag==1){
			alert("修改成功！！");
		}else{
			alert("修改失败！！");
		}
	}
	//点击保存时执行的方法
	function addSubmit(){
		//验证表单数据
	 	if(!submitForm("fm")){
		 	return;
		 }
		var urls=context+"/crm/customer/CustomerManage/customerAdd.json";
		makeFormCall(urls, addResult, "fm") ;
	}
	//保存的回调
	function addResult(json){
		if(json.flag==0){
			alert("新增失败！！");
		}else{
			document.getElementById("ctmId").value=json.flag;
			alert("新增成功！！");
		}
	}
	
	function myLinkOp(log_id,meta,record){
		return String.format("<input type='checkbox' onclick='checkBoxSelect("+record.data.LOG_ID+",this)'/>");
	}
	//点击新增执行的方法
	function addPre(){
		window.location.href=context+"/crm/customer/CustomerManage/addPre.do";
	}
	//试乘试驾选择发生改变执行 的方法
	function doDrivingChange(){
		var ifDriving=$("ifDriving").value
		if(ifDriving==10041001){
			document.getElementById("drivingBtn").style.display="inline";
		}else{
			document.getElementById("drivingBtn").style.display="none";
		}
	}
	//修改的时候加载省市区
	function genAddrData(){
		var provinceId=$("provinceId").value;
		var cityId=$("cityId").value;
		var townId=$("townId").value;
		genLocSel('dPro','dCity','dArea',provinceId,cityId,townId); // 加载省份城市和区县
		
	}

	//意向车型
	function toIntentVechileList(){
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toIntentVechileList.do',800,600);
	}
	 //显示意向车型
	function showInfo(pose_id,pose_name){
		document.getElementById("vechile_name").value = pose_name;
		document.getElementById("intentVechile").value = pose_id;
	}
	
	function clrTxt(){
		document.getElementById("vechile_name").value = "";
		document.getElementById("intentVechile").value = "";
	}
	
	//保存竞品
	function saveCompet(){
		var ctmId=document.getElementById("ctmId").value;
		setScrollTop();
		if(ctmId==null||""==ctmId){
			alert("请先保存主表数据！！！");
			return;
		}
		var context=$("curPaths").value;
		var urls=context+"/crm/customer/CustomerManage/saveCompet.json?ctmId="+ctmId;
		makeFormCall(urls, saveResult, "fm") ;
	}
	
	function saveResult(json){
		if(json.flag==1){
			//document.getElementById("ctmId").value=json.flag;
			alert("保存竞品成功！！");
		}else{
			alert("保存竞品失败！！");
		}
	}
	
	//竞品车型
	function toCompetVechileList(level){
		var context=$("curPaths").value;
		$("competLevel").value=level;
		var level=$("competLevel").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toCompetVechileList.do?level='+level,800,600);
	}
	 //竞品车型
	function showCompetInfo(compet_id,compet_name){
		var level=$("competLevel").value
		if(level==2){
			document.getElementById("compet_name").value = compet_name;
			document.getElementById("competVechile").value = compet_id;
		}else{
			document.getElementById("other_name").value = compet_name;
			document.getElementById("otherProduct").value = compet_id;
		}
		
	}
	
	function clrCompetTxt(){
		var level=$("competLevel").value
			if(level==2){
				document.getElementById("compet_name").value = "";
				document.getElementById("competVechile").value = "";
			}else{
				document.getElementById("other_name").value = "";
				document.getElementById("otherProduct").value = "";
			}
	}
	function linkCustomer(value,meta,record){
		var ctmId=record.data.CUSTOMER_ID;
		var str="";
		str+="<input type='checkbox' name='ctms' value='"+ctmId+"'/>";
		return String.format(str) ;
	}
	//批量修改顾问
	function changeAdviserAdd(){
		var context=$("curPaths").value;
		 var customerIds=document.getElementsByName("ctms");
		 var str="";
		 for(var i=0;i<customerIds.length;i++){
			 var customerId=customerIds[i];
			 	if(customerId.checked){
			 		if(i==customerIds.length-1){
			 			str+=customerId.value;
			 		}else{
			 			str+=customerId.value+",";
			 		}
			 	}
		 }
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/changeAdviserInit.do?ctmIds='+str,400,200);
	}
	function orderTaskFunc(sortId,customerId){
		var telephone=$("telephone").value;
		var flag=judgeIfAbleOderDate(telephone);
		if(!flag){
			alert("DCRC未录入该客户今日到店客流信息，不能做订车计划！！！");
			return ;
		}
		var url=null;
		url=context+"/crm/taskmanage/TaskManage/doTaskOrderInit.do?taskId="+sortId+"&customerId="+customerId+"" ;
		location.href=url;
	}
	