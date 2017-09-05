/**
* 交车确认的js  yinshunhui 2014-11-25
*
*/
	var context=null;
	var myPage;
	var url = null;
	var title = null;
	var userType=null;
	var columns =null;
	//定义列
	 columns = [
				{header: "客户名称", dataIndex:'CUSTOMER_NAME',align:'center' },
				{header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车型", dataIndex: 'MODEL', align:'center'},
				{header: "颜色", dataIndex: 'COLOR', align:'center'},
				{header: "价格", dataIndex: 'PRICE', align:'center'},
				{header: "数量", dataIndex: 'NUM', align:'center'},
				{header: "交车日期", dataIndex: 'DELIVERY_DATE', align:'center'},
				{header: "已交车数量", dataIndex: 'DELIVERY_NUMBER', align:'center'},
				{header: "操作", dataIndex: 'CUSTOMER_ID', align:'center',renderer:delvInit}
		      ];
	//进入页面执行的方法
	function doInit(){
		context=document.getElementById("curPaths").value;
		url=globalContextPath+"/crm/delivery/DelvManage/delvQueryList.json?COMMAND=1";
		userType=$('userType').value;
		if(userType=="60281002" || userType=="60281003"){
			//定义列
		columns = [
				  {header: "客户名称", dataIndex:'CUSTOMER_NAME',align:'center' },
				  {header: "手机", dataIndex: 'TELEPHONE', align:'center'},
				  {header: "VIN", dataIndex: 'VIN', align:'center'},
				  {header: "车型", dataIndex: 'MODEL', align:'center'},
				  {header: "颜色", dataIndex: 'COLOR', align:'center'},
				  {header: "价格", dataIndex: 'PRICE', align:'center'},
				  {header: "数量", dataIndex: 'NUM', align:'center'},
				  {header: "交车日期", dataIndex: 'DELIVERY_DATE', align:'center'},
				  {header: "已交车数量", dataIndex: 'DELIVERY_NUMBER', align:'center'}
					      ];
			
		}
		__extQuery__(1);
	} 
	//初始化交车链接
	function delvInit(value,meta,record){
		var vechile_id=record.data.VEHICLE_ID;
		var order_detail_id=record.data.ORDER_DETAIL_ID;
		var telephone=record.data.TELEPHONE;
		var str="";
		str+="<a href='#' onclick='delv(\""+ telephone +"\",\""+ vechile_id +"\",\""+ value +"\",\""+ order_detail_id +"\")'>[交车]</a>/<a href='#' onclick='printDelv(\""+ vechile_id +"\",\""+ value +"\",\""+ order_detail_id +"\")'>[打印]</a>";
		return String.format(str) ;
	}
	//点击页面交车的时候执行的js方法
	function delv(telephone,vechile_id,qkId,qkOrderDetailId) {
		if(!confirm("确认交车吗？")){
			return;
		}
		var flag=judgeIfAbleOderDate(telephone);
		if(!flag){
			alert("DCRC未录入该客户今日到店客流信息，不能做交车！！！");
			return;
		}
		var context=document.getElementById("curPaths").value;
		var scrollHeight = document.getElementById("show").offsetHeight;//获取显示页面div的高度
		//vechile_id为无效值表示车架号不存在的情况
		if(null==vechile_id||'null'==vechile_id||""==vechile_id){
		    OpenHtmlWindow(context+'/crm/delivery/DelvManage/toVinList.do?qkId='+qkId+'&qkOrderDetailId='+qkOrderDetailId,800,600);
		}else{
		addDelvData(vechile_id,qkId,qkOrderDetailId);
		addMaskLayer(scrollHeight);//添加蒙层
		}
	}
	
	//点击打印时执行的方法
	function printDelv(vechile_id,customer_id,order_detail_id){
		$('fm').action= globalContextPath+"/crm/delivery/DelvManage/printDelvInit.do";
	 	$('fm').target="_blank";
	 	$('fm').submit();
	}
	
	 //选择了车架号之后跳转到实销上报页面
	//function showCompetInfo(vechile_id,qkId,qkOrderDetailId){
	//		var context=$("curPaths").value;
	//		var urls = context+"/sales/customerInfoManage/SalesReport/toReport.do?vehicle_id="+vechile_id+"&qkId="+qkId+"&qkOrderDetailId="+qkOrderDetailId;
	//		location.href=urls;
    //	}
	
	function addDelvData(vechile_id,qkId,qkOrderDetailId){
		var context=document.getElementById("curPaths").value;
		var urls=globalContextPath+"/crm/delivery/DelvManage/addDelvData.json?vehicle_id="+vechile_id+"&qkId="+qkId+"&qkOrderDetailId="+qkOrderDetailId;
		makeFormCall(urls,addResult,'fm');
	}
	function addResult(json){
		if(json.flag=='1'){
			alert("操作成功！！！");
			var urls = globalContextPath+"/crm/delivery/DelvManage/doSureInit.do";
			location.href=urls;
		}else{
			alert("操作失败！！！");
		}
	}
	
	//验证是否可以做订单false不可以   true可以做订单（当日是否有到店）
	function judgeIfAbleOderDate(tel){
		var url=globalContextPath+"/crm/taskmanage/TaskManage/judgeIfAbleOrderDate.json?telephone="+tel;
		var flag=false;
		makeSameCall(url,orderBack,"fm");
		function orderBack(json){
			if(json.flag=="1"){
				flag=true;
			}
		}
		return flag;
	}
	
