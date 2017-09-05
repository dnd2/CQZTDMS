//试乘试驾信息修改部分start
	 function drivingTableAdd(json){
	 		deleteDriveRows();
	 	for(var i=0;i<json.driveList.length;i++){
	 		var len=document.getElementById("drivingTable").rows.length;
			var newTR = document.getElementById("drivingTable").insertRow(1);
            var newNameTD = newTR.insertCell(0);
            newNameTD.innerHTML =json.driveList[i].CARD_NO;
            var newNameTD = newTR.insertCell(1);
            newNameTD.innerHTML=json.driveList[i].DRIVING_DATE;
            var newNameTD = newTR.insertCell(2);
            newNameTD.innerHTML =json.driveList[i].DRIVING_VECHILE;
            var newNameTD = newTR.insertCell(3);
            newNameTD.innerHTML = json.driveList[i].DRIVING_MAN;
         //   var newNameTD = newTR.insertCell(4);
          //  newNameTD.innerHTML =json.driveList[i].DRIVING_ROAD;
            var newNameTD = newTR.insertCell(4);
            newNameTD.innerHTML = json.driveList[i].FIRST_MILE;
            var newNameTD = newTR.insertCell(5);
            newNameTD.innerHTML = json.driveList[i].END_MILE;
            var newNameTD = newTR.insertCell(6);
           	var str="";
	        str+="<a href='###' onclick='"+'deleteDriveRow("'+json.driveList[i].DRIVING_ID+'",this);'+"' align='center'>删&nbsp;&nbsp;除</a>/";
	        str+="<a href='###'   onClick='"+'toDriveUpdate("'+json.driveList[i].DRIVING_ID+'");'+"' align='center'>修&nbsp;&nbsp;改</a>";
	        newNameTD.innerHTML =str ;
        }
        window.scrollTo(0,$("top").value);  
	}
	//添加
	function toDriveAdd(){
		var ctmId=document.getElementById("ctmId").value;
		setScrollTop();
		if(ctmId==null||""==ctmId){
			alert("请先保存主表数据！！！");
			return;
		}
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toDriveAddInit.do?ctmId='+ctmId,800,400);
	}
	//删除除表头外的所有行
	function deleteDriveRows(){
		var ks=document.getElementById("drivingTable");
		var rows=ks.rows.length;
		if(rows>1){
			for(var i=1;i<rows;){
				ks.deleteRow(i);
				--rows;
			}
		}
	}
	
	function deleteDriveRow(driveId,obj){
		var rowIndex=obj.parentNode.parentNode.rowIndex;
		setScrollTop();
		//ajax删除数据
		var context=$("curPaths").value;
		var urls=context+'/crm/customer/CustomerManage/driveDelete.json?driveId='+driveId+'&rowIndex='+rowIndex;
		makeFormCall(urls, deleteDriveResult, "fm") ;
		
	}
	
	function deleteDriveResult(json){
		var rowIndex=json.rowIndex;
		if(rowIndex>0){
			var ks=document.getElementById("drivingTable");
			ks.deleteRow(rowIndex);
		}else{
			alert("操作失败！！！");
		}
		 window.scrollTo(0,$("top").value);   
	}
	
	//修改车辆信息
	function toDriveUpdate(driveId){
		setScrollTop();
		var ctmId=document.getElementById("ctmId").value;
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toDriveUpdateInit.do?ctmId='+ctmId+'&driveId='+driveId,800,400);
	}
	
	