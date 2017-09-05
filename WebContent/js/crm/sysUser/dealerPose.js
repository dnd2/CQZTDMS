

 //查询职位
	function toPoseList(){
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/sysUser/DealerSysPose/toPoseList.do',800,600);
	}
	 //显示职位
	function showSalesManInfo(pose_id,pose_name){
		document.getElementById("par_pose_name").value = pose_name;
		document.getElementById("par_pose_id").value = pose_id;
	}
	
	function clrTxt(){
		document.getElementById("par_pose_name").value = "";
		document.getElementById("par_pose_id").value = "";
	}