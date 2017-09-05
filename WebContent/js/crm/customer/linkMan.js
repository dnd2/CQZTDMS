 //添加表中数据
 function linkTableAdd(json){
 			deleteLinkRows();
 			for(var i=0;i<json.linkList.length;i++){
	 			var len=document.getElementById("linkTable").rows.length;
				var newTR = document.getElementById("linkTable").insertRow(1);
	            var newNameTD = newTR.insertCell(0);
	            newNameTD.innerHTML =json.linkList[i].LINK_MAN;
	            var newNameTD = newTR.insertCell(1);
	            newNameTD.innerHTML =json.linkList[i].LINK_PHONE;
	            var newNameTD = newTR.insertCell(2);
	            newNameTD.innerHTML = json.linkList[i].CARD_TYPE;
	            var newNameTD = newTR.insertCell(3);
	            newNameTD.innerHTML =json.linkList[i].CARD_CODE;
	            var newNameTD = newTR.insertCell(4);
	            newNameTD.innerHTML =json.linkList[i].RELATIONSHIP;
	            var newName = newTR.insertCell(5);
	             if(json.vehicleList[i].OP_TYPE=='1'){
		             var str="";
	            	str+="<a href='###' onclick='"+'deleteLinkRow("'+json.linkList[i].LINK_ID+'",this);'+"' align='center'>删&nbsp;&nbsp;除</a>/";
	            	str+="<a href='###'   onClick='"+'toLinkUpdate("'+json.linkList[i].LINK_ID+'");'+"' align='center'>修&nbsp;&nbsp;改</a>";
	            	newName.innerHTML =str ;
            	}
 		  }
 		  window.scrollTo(0,$("top").value);
	}
	
	//跳转到添加联系人
	function toLinkAdd(){
		var ctmId=document.getElementById("ctmId").value;
		if(ctmId==null||""==ctmId){
			alert("请先保存主表数据！！！");
			return;
		}
		setScrollTop();
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toLinkAddInit.do?ctmId='+ctmId,800,600);
	}
	//删除去表头的所有行
	function deleteLinkRows(){
		var ks=document.getElementById("linkTable");
		var rows=ks.rows.length;
		if(rows>1){
			for(var i=1;i<rows;){
				ks.deleteRow(i);
				--rows;
			}
		}
		
	}
	
	function deleteLinkRow(linkId,obj){
		var rowIndex=obj.parentNode.parentNode.rowIndex;
		//ajax删除数据
		var context=$("curPaths").value;
		var urls=context+'/crm/customer/CustomerManage/linkDelete.json?linkId='+linkId+'&rowIndex='+rowIndex;
		makeFormCall(urls, deleteLinkResult, "fm") ;
		
	}
	
	function deleteLinkResult(json){
		var rowIndex=json.rowIndex;
		if(rowIndex>0){
			var ks=document.getElementById("linkTable");
			ks.deleteRow(rowIndex);
		}else{
			alert("操作失败！！！");
		}
		window.scrollTo(0,$("top").value);
	}
	
	//修改联系人
	function toLinkUpdate(linkId){
		setScrollTop();
		var ctmId=document.getElementById("ctmId").value;
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toLinkUpdateInit.do?ctmId='+ctmId+'&linkId='+linkId,800,600);
	}