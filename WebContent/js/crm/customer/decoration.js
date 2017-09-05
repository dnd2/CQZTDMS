//装饰装潢部分start
	 function docTableAdd(json){
	 	deleteDocRows();
	 	 for(var i=0;i<json.docList.length;i++){
	 		var len=document.getElementById("docTable").rows.length;
			var newTR = document.getElementById("docTable").insertRow(1);
            var newNameTD = newTR.insertCell(0);
            newNameTD.innerHTML =json.docList[i].EXPROJECT;
            var newNameTD = newTR.insertCell(1);
            newNameTD.innerHTML=json.docList[i].EXNAME;
            var newNameTD = newTR.insertCell(2);
            newNameTD.innerHTML =json.docList[i].AMOUNT;
            var newNameTD = newTR.insertCell(3);
            newNameTD.innerHTML =json.docList[i].PRICE;
            var newNameTD = newTR.insertCell(4);
            newNameTD.innerHTML = json.docList[i].MONEY;
              var newNameTD = newTR.insertCell(5);
            newNameTD.innerHTML = json.docList[i].GIVEORBUY;
            var newName = newTR.insertCell(6);
            var str="";
            str+="<a href='###' onclick='"+'deleteDocRow("'+json.docList[i].DECORATION_ID+'",this);'+"' align='center'>删&nbsp;&nbsp;除</a>/";
            str+="<a href='###'   onClick='"+'toDocUpdate("'+json.docList[i].DECORATION_ID+'");'+"' align='center'>修&nbsp;&nbsp;改</a>";
            newName.innerHTML =str ;
           }
            window.scrollTo(0,$("top").value);  
	}
	function deleteDocRow(docId,obj){
		var rowIndex=obj.parentNode.parentNode.rowIndex;
		//var ks=document.getElementById("docTable");
		//ks.deleteRow(rowIndex);
		//var rowIndex=obj.parentNode.parentNode.rowIndex;
		//ajax删除数据
		var context=$("curPaths").value;
		setScrollTop();
		var urls=context+'/crm/customer/CustomerManage/deleteDoc.json?docId='+docId+'&rowIndex='+rowIndex;
		makeFormCall(urls, deleteDocResult, "fm") ;
	}
	
	function deleteDocResult(json){
		var rowIndex=json.rowIndex;
		if(rowIndex>0){
			var ks=document.getElementById("docTable");
			ks.deleteRow(rowIndex);
		}else{
			alert("操作失败！！！");
		}
		window.scrollTo(0,$("top").value); 
	}
	//end
		//删除去表头以外装饰的数据的所有行
	function deleteDocRows(){
		var ks=document.getElementById("docTable");
		var rows=ks.rows.length;
		if(rows>1){
			for(var i=1;i<rows;){
				ks.deleteRow(i);
				--rows;
			}
		}
	}
	//添加装饰信息
	function toDecorationAdd(){
		var ctmId=document.getElementById("ctmId").value;
		setScrollTop();
		if(ctmId==null||""==ctmId){
			alert("请先保存主表数据！！！");
			return;
		}
		var context=$("curPaths").value;
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toDocAddInit.do?ctmId='+ctmId,800,600);
	}
	//查询职位
	function toDocUpdate(docId){
		var ctmId=document.getElementById("ctmId").value;
		var context=$("curPaths").value;
		setScrollTop();
		OpenHtmlWindow(context+'/crm/customer/CustomerManage/toDocUpdateInit.do?ctmId='+ctmId+'&docId='+docId,800,600);
	}