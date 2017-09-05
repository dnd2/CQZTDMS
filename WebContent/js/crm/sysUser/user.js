 //查询职位
	function toPoseList(){
		var context=$("curPaths").value;
		var userId=$("USER_ID").value;
		var poseRank=$("poseRank").value;
		OpenHtmlWindow(context+'/crm/sysUser/DealerSysUser/toUserList.do?userId='+userId+'&poseRank='+poseRank,800,600);
	}
	 //显示职位
	function showSalesManInfo(user_id,user_name){
		document.getElementById("par_user_name").value = user_name;
		document.getElementById("par_user_id").value = user_id;
	}
	
	function clrTxt(){
		document.getElementById("par_user_name").value = "";
		document.getElementById("par_user_id").value = "";
	}
	//点击添加职位是执行的方法
	function toPoseAddList(){
		var context=$("curPaths").value;
		var poseRank=$("poseRank").value;
		OpenHtmlWindow(context+'/crm/sysUser/DealerSysUser/toUserList.do?poseRank='+poseRank,800,600);
	}
	//是否锁定下拉列表选择时执行的方法
	function loadIsLock(obj){
		document.getElementById("isLock").value=obj.getAttribute("TREE_ID");
	}
	//点击职位级别是执行的方法
	function loadPoseRank(obj){
		$("poseRank").value=obj.getAttribute("TREE_ID");
		if(obj.getAttribute("TREE_ID")=='60281002'||obj.getAttribute("TREE_ID")=='60281001'||obj.getAttribute("TREE_ID")=='60281005'){
			$("groupTd1").style.display='none';
			$("groupTd2").style.display='none';
		}else{
			$("groupTd1").style.display='block';
			$("groupTd2").style.display='block';
		}
		 getParId();
		
	}
	//点击性别执行的方法
	function loadGender(obj){
		$("GENDER").value=obj.getAttribute("TREE_ID");
	}
	//点击状态时执行的方法
	function loadStatus(obj){
		$("USER_STATUS").value=obj.getAttribute("TREE_ID");
	}
	

	//获取当前用户的上级人员
	function getParId(){
		var poseRank=$("poseRank").value;
		var groupId=$("groupId").value;
		var context=$("curPaths").value;
		//如果不是销售顾问ajax不传groupId
		alert(poseRank)
		if(poseRank	!=60281004){
			groupId='';
			//如果不是销售主管那么groupId下拉列表的值为空
			if(poseRank!=60281003){
				alert("60281003");
				$("groupId").value='';
			}
		}
		var urls=context+'/crm/sysUser/DealerSysUser/getParId.json?poseRank='+poseRank+'&groupId='+groupId;
		makeFormCall(urls, getResult, "fm") ;
	}
	
	function getResult(json){
		//alert(JSON.stringify(json));
		if(json.flag=='1'){
			if(json.tu.name==null || json.tu.name==''){
				$("par_user_name").value='';
			}else{
				$("par_user_name").value=json.tu.name;
			}
			if(json.tu.userId==null || json.tu.userId==''){
				$("par_user_id").value='';
			}else{
				$("par_user_id").value=json.tu.userId;
			}
		}
	}
	