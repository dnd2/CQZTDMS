//保险部分start
	 function insureTableAdd(json){
	 	deleteRows();
	 	 for(var i=0;i<json.insureList.length;i++){
			var newTR = document.getElementById("insureTable").insertRow(1);
            var newNameTD = newTR.insertCell(0);
            newNameTD.innerHTML =json.insureList[i].INSURENCE_COMPANY;
            var newNameTD = newTR.insertCell(1);
            newNameTD.innerHTML=json.insureList[i].INSURENCE_DATE;
            var newNameTD = newTR.insertCell(2);
            newNameTD.innerHTML =json.insureList[i].INSURENCE_VAR;
            var newNameTD = newTR.insertCell(3);
            newNameTD.innerHTML =json.insureList[i].INSURENCE_MONEY;
            var newNameTD = newTR.insertCell(4);
            newNameTD.innerHTML = json.insureList[i].REMARK;
            var newName = newTR.insertCell(5);
            var str="";
            str+="<a href='###' onclick='"+'deleteInsureRow("'+json.insureList[i].INSURENCE_ID+'",this);'+"' align='center'>删&nbsp;&nbsp;除</a>/";
            str+="<a href='###'   onClick='"+'toInsureUpdate("'+json.insureList[i].INSURENCE_ID+'");'+"' align='center'>修&nbsp;&nbsp;改</a>";
            newName.innerHTML =str ;
	 	 }
	 	window.scrollTo(0,$("top").value); 
	}
	function deleteInsureRow(insureId,obj){
		var rowIndex=obj.parentNode.parentNode.rowIndex;
		//ajax删除数据
		var context=$("curPaths").value;
		setScrollTop();
		var urls=context+'/crm/customer/CustomerManage/deleteInsure.json?insureId='+insureId+'&rowIndex='+rowIndex;
		makeFormCall(urls, deleteInsureResult, "fm") ;
	}
	
	function deleteInsureResult(json){
		var rowIndex=json.rowIndex;
		if(rowIndex>0){
			var ks=document.getElementById("insureTable");
			ks.deleteRow(rowIndex);
		}else{
			alert("操作失败！！！");
		}
		window.scrollTo(0,$("top").value);
	}
	
	//删除去表头以外的所有行
	function deleteRows(){
		var ks=document.getElementById("insureTable");
		var rows=ks.rows.length;
		if(rows>1){
			for(var i=1;i<rows;){
				ks.deleteRow(i);
				--rows;
			}
		}
	}
	//添加保险公司
	function toInsureAdd(){
		var ctmId=document.getElementById("ctmId").value;
		if(ctmId==null||""==ctmId){
			alert("请先保存主表数据！！！");
			return;
		}
		setScrollTop();
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toInsureAddInit.do?ctmId='+ctmId,800,600);
	}
	//查询职位
	function toInsureUpdate(insureId){
		setScrollTop();
		var ctmId=document.getElementById("ctmId").value;
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toInsureUpdateInit.do?ctmId='+ctmId+'&insureId='+insureId,800,600);
	}
	