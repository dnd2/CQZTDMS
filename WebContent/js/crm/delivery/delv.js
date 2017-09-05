/**
* 交车的js  yinshunhui 2014-11-25
*
*/
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
				{header: "交车日期", dataIndex: 'DELIVERY_DATE', align:'center'},
				{header: "操作", dataIndex: 'CUSTOMER_ID', align:'center',renderer:delvInit}
		      ];
	//进入页面执行的方法
	function doInit(){
		context=document.getElementById("curPaths").value;
		url=context+"/crm/delivery/DelvManage/delvCheckQueryList.json?COMMAND=1";
		__extQuery__(1);
	} 
	//初始化交车链接
	function delvInit(value,meta,record){
		var vechile_id=record.data.VEHICLE_ID;
		var order_detail_id=record.data.ORDER_DETAIL_ID;
		//已经交车退车的数据操作退车
		//var life_cycle=record.data.LIFE_CYCLE;
		var delivery_status=record.data.DELIVERY_STATUS;
		var delvDetailId=record.data.DELV_DETAIL_ID;
		//已经交车没有上报的做上报按钮
		var str="";
		//如果刚刚做交车
		if(delivery_status=="60571001"){
			str+="<a href='#' onclick='delv(\""+ vechile_id +"\",\""+ value +"\",\""+ order_detail_id +"\",\""+ delvDetailId +"\")'>[上报]</a>/<a href='#' onclick='returnVechile(\""+ vechile_id +"\",\""+ value +"\",\""+ order_detail_id +"\",\""+ delvDetailId +"\")'>[退车]</a>";
		//交车完成后做了退车
		}else if (delivery_status=="60571003"){
			str+="<a href='#' onclick='returnVechile(\""+ vechile_id +"\",\""+ value +"\",\""+ order_detail_id +"\",\""+ delvDetailId +"\")'>[退车]</a>";
		}
		return String.format(str) ;
	}
	//退车
	function returnVechile(vechile_id,ctmId,order_detail_id,delvDetailId){
			if(!confirm("确认退车吗？")){
				return;
			}
			var url=context+"/crm/delivery/DelvManage/returnVechile.json?vehicle_id="+vechile_id+"&ctmId="+ctmId+"&orderDetailId="+order_detail_id+"&delvDetailId="+delvDetailId;
			makeFormCall(url, SubmitTip, "fm") ;
	}
	//点击确认退车后执行的方法
function SubmitTip(json) {
	var subFlag = json.subFlag ;
	if(subFlag == '1') {
		alert("退车成功!") ;
		__extQuery__(1);
	} else if(subFlag=="2"){
		alert("退车失败!订单处于锁定状态！！！") ;
	}else{
		alert("退车失败！！请联系管理员！！");
	}
}
	//点击页面交车的时候执行的js方法
	function delv(vechile_id,qkId,qkOrderDetailId,delvDetailId){
		if(!confirm("确认上报吗？")){
			return;
		}
		var context=$("curPaths").value;
		//vechile_id为无效值表示车架号不存在的情况
		if(null==vechile_id||'null'==vechile_id||""==vechile_id){
		   // OpenHtmlWindow(context+'/crm/delivery/DelvManage/toVinList.do?qkId='+qkId+'&qkOrderDetailId='+qkOrderDetailId,800,600);
		}else{
			var urls = context+"/sales/customerInfoManage/SalesReport/toReport.do?vehicle_id="+vechile_id+"&qkId="+qkId+"&qkOrderDetailId="+qkOrderDetailId+"&delvDetailId="+delvDetailId;
			location.href=urls;
		}
	}
	
	 //选择了车架号之后跳转到实销上报页面
	function showCompetInfo(vechile_id,qkId,qkOrderDetailId){
			var context=$("curPaths").value;
			var urls = context+"/sales/customerInfoManage/SalesReport/toReport.do?vehicle_id="+vechile_id+"&qkId="+qkId+"&qkOrderDetailId="+qkOrderDetailId;
			location.href=urls;
		
	}
	
	
	
	
