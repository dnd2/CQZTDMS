	var context=null;
	var customerId=null;
	var myPage;
	var url = null;
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex},
				{header: "任务类型", dataIndex: 'TASK_TYPE', align:'center'},
				{header: "客户名称", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "联系电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "意向等级", dataIndex: 'NEW_LEVEL', align:'center'},
				{header: "任务内容", dataIndex: 'TASK_INFO', align:'center'},
				{header: "完成时间", dataIndex: 'FINISH_DATE', align:'center'}
		      ];

	function doInit(){
		context=$('curPaths').value;
		customerId=$('customerId').value;
		url=context+"/crm/customerContactRecord/CustomerContactRecord/customerQueryList.json?COMMAND=1&customerId="+customerId;
		__extQuery__(1);
	} 
	//下拉列表选择时执行的方法
	function loadStatus(obj) {
		document.getElementById("status").value = obj.getAttribute("TREE_ID");
	}

	