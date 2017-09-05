	var context=null;
	var myPage;
	var url = null;
	var title = null;
	//定义列	
	var columns = [
				{header: "客户名称", dataIndex:'CUSTOMER_NAME',align:'center' },
				{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车型", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "颜色", dataIndex: 'COLOR_NAME', align:'center'},
				{header: "价格", dataIndex: 'PRICE', align:'center'},
				{header: "状态", dataIndex: 'TYPES', align:'center',renderer:ifDelvLink},
				{header: "交车日期", dataIndex: 'DELIVERY_DATE', align:'center'}
		      ];
	//进入页面执行的方法
	function doInit(){
		context=document.getElementById("curPaths").value;
		url=context+"/crm/delivery/DelvManage/delvAllQueryList.json?COMMAND=1";
		__extQuery__(1);
	} 
	 
	
	 //选择了车架号之后跳转到实销上报页面
	function showCompetInfo(vechile_id,qkId,qkOrderDetailId){
			var context=$("curPaths").value;
			var urls = context+"/sales/customerInfoManage/SalesReport/toReport.do?vehicle_id="+vechile_id+"&qkId="+qkId+"&qkOrderDetailId="+qkOrderDetailId;
			location.href=urls;
		
	}
	//交车和上报
	function ifDelvLink(value,meta,data){
		var str="";
		if(value=="60571001"){
			str="已交车";
		}else if(value=="60571002"){
			str+="已上报";
		}else if(value=="60571003"){
			str+="待退车";
		}else if(value=="60571004"){
			str+="已退车";
		}
		return String.format(str);
	}
	
	
	
	
