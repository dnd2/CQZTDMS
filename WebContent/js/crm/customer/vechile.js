//车辆信息部分 start
	 function vechileTableAdd(json){
	        deleteVehicleRows();
	        for(var i=0;i<json.vehicleList.length;i++){
		 		var len=document.getElementById("vechileTable").rows.length;
				var newTR = document.getElementById("vechileTable").insertRow(1);
	            var newNameTD = newTR.insertCell(0);
	            newNameTD.innerHTML =json.vehicleList[i].VIN;
	            var newNameTD = newTR.insertCell(1);
	            newNameTD.innerHTML =json.vehicleList[i].MODEL_CODE;
	            var newNameTD = newTR.insertCell(2);
	            newNameTD.innerHTML =json.vehicleList[i].MODEL_NAME;
	            var newNameTD = newTR.insertCell(3);
	            newNameTD.innerHTML=json.vehicleList[i].BUY_DATE;
	            var newNameTD = newTR.insertCell(4);
	            newNameTD.innerHTML =json.vehicleList[i].VECHILE_COLOR ;
	            var newNameTD = newTR.insertCell(5);
	            newNameTD.innerHTML =json.vehicleList[i].LOW_VIN;
	            var newNameTD = newTR.insertCell(6);
	            newNameTD.innerHTML =json.vehicleList[i].PRICE ;
	            var newNameTD = newTR.insertCell(7);
	            newNameTD.innerHTML =json.vehicleList[i].CAR_NUMBER;
	             var newNameTD = newTR.insertCell(8);
	            newNameTD.innerHTML=json.vehicleList[i].BOARD_DATE;
	            var newNameTD = newTR.insertCell(9);
	            newNameTD.innerHTML =json.vehicleList[i].PIN;
	            var newNameTD = newTR.insertCell(10);
	            newNameTD.innerHTML=json.vehicleList[i].PRODUCT_DATE;
	            var newName = newTR.insertCell(11);
	            if(json.vehicleList[i].OP_TYPE=='1'){
	            	 var str="";
		            str+="<a href='###' onclick='"+'deleteVehicleRow("'+json.vehicleList[i].VECHILE_ID+'",this);'+"' align='center'>删&nbsp;&nbsp;除</a>/";
		            str+="<a href='###'   onClick='"+'toVehicleUpdate("'+json.vehicleList[i].VECHILE_ID+'");'+"' align='center'>修&nbsp;&nbsp;改</a>";
		            newName.innerHTML =str ;
	            }
	           
        }
        window.scrollTo(0,$("top").value);
	}
	//跳转到添加联系人
	function toVehicleAdd(){
		var ctmId=document.getElementById("ctmId").value;
		if(ctmId==null||""==ctmId){
			alert("请先保存主表数据！！！");
			return;
		}
		setScrollTop();
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toVehicleAddInit.do?ctmId='+ctmId,800,600);
	}
	//删除去表头的所有行
	function deleteVehicleRows(){
		var ks=document.getElementById("vechileTable");
		var rows=ks.rows.length;
		if(rows>1){
			for(var i=1;i<rows;){
				ks.deleteRow(i);
				--rows;
			}
		}
	}
	
	function deleteVehicleRow(vehicleId,obj){
		var rowIndex=obj.parentNode.parentNode.rowIndex;
		setScrollTop();
		//ajax删除数据
		var context=$("curPaths").value;
		var urls=context+'/crm/customer/CustomerManage/vehicleDelete.json?vehicleId='+vehicleId+'&rowIndex='+rowIndex;
		makeFormCall(urls, deleteVehicleResult, "fm") ;
		
	}
	
	function deleteVehicleResult(json){
		var rowIndex=json.rowIndex;
		if(rowIndex>0){
			var ks=document.getElementById("vechileTable");
			ks.deleteRow(rowIndex);
		}else{
			alert("操作失败！！！");
		}
		 window.scrollTo(0,$("top").value);
	}
	
	//修改车辆信息
	function toVehicleUpdate(vehicleId){
		setScrollTop();
		var ctmId=document.getElementById("ctmId").value;
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toVehicleUpdateInit.do?ctmId='+ctmId+'&vehicleId='+vehicleId,800,600);
	}