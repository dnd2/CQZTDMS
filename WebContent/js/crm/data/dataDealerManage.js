	var context=null;
	var myPage;
	var url = null;
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex},
				{header: "机构代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "机构名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "父级名称", dataIndex: 'TYPE_NAME', align:'center'},
				{header: "本级代码", dataIndex: 'CODE_ID', align:'center'},
				{header: "本级名称", dataIndex: 'CODE_DESC', align:'center'},
				{header: "级数", dataIndex: 'CODE_LEVEL', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作", dataIndex: 'CODE_ID', align:'center',renderer:dateUpdateInit}
		      ];
	function doInit(){
		context=$('curPaths').value
		url=context+"/crm/data/DataManage/dataDealerQueryList.json?COMMAND=1"
		__extQuery__(1);
	} 
	
	function dateUpdateInit(value,meta,record){
		var code_id=record.data.CODE_ID;
		//判断是否经销商可以维护
		var ifDealer=record.data.IF_DEALER;
		var str="";
		if(ifDealer=='10041001'){
			str+="<a href='#' onclick='dataUpdate(\""+ code_id +"\")'>[修改]</a>/<a href='#' onclick='dataSelect(\""+ code_id +"\")'>[查看]</a>";
		}else{
			str+="<a href='#' onclick='dataSelect(\""+ code_id +"\")'>[查看]</a>";
		}
		
		return String.format(str) ;
	}
	
	function dataUpdate(codeId){
		var urls = context+"/crm/data/DataManage/dataDealerModifyInit.do?codeId="+codeId;
		location.href=urls;
	}
	function dataSelect(codeId){
		var urls = context+"/crm/data/DataManage/dataSelectInit.do?codeId="+codeId;
		location.href=urls;
	}
	function saveData(){
		var array=document.getElementsByName("nextCodeDesc");
		var flag=0;
		for(var i=0;i<array.length;i++){
			if(array[i].value==null){
				flag=1;
				break;
			}
			if(array[i].value!=null&&array[i].value.replace(/[ ]/g,"")==""){
				flag=1;
				break;
			}
		}
		if(flag==1){
			MyAlert("子级字典名称不能为空！！！");
			return;
		}
		var context=$('curPaths').value;
		var urls=context+"/crm/data/DataManage/saveDealerData.json";
		makeFormCall(urls, saveResult, "fm") ;
	}
	function saveResult(json){
		if(json.flag==1){
			alert("操作成功！！");
			var codeName=document.getElementById("codeName").value;
			location.href=context+"/crm/data/DataManage/doDealerInit.do?codeName="+codeName;
		}else{
			alert("操作失败！！");
		}
	}
	
	
	//操作table的相关方法
	function tableBtn(){
			var ks=document.getElementById("nextTable");
			var rows=ks.rows.length;
			var newTR = document.getElementById("nextTable").insertRow(rows);
			var dealerCode=document.getElementById("dealerCode").value;
			var dealerName=document.getElementById("dealerName").value;
			var newNameTD = newTR.insertCell(0);
			newNameTD.style.cssText="BORDER:   black   1px   solid";
            newNameTD.innerHTML=dealerCode;
            var newNameTD = newTR.insertCell(1);
            newNameTD.style.cssText="BORDER:   black   1px   solid";
            newNameTD.innerHTML=dealerName;
            var newNameTD = newTR.insertCell(2);
            newNameTD.style.cssText="BORDER:   black   1px   solid";
            var parCode="&nbsp;<input type='hidden' name='nextCodeId' value='' />";
            newNameTD.innerHTML = parCode;
            var newNameTD = newTR.insertCell(3);
            newNameTD.style.cssText="BORDER:   black   1px   solid";
            newNameTD.innerHTML = "<input name='nextCodeDesc' id='nextCodeDesc' type='text'  style='width:100%'/>";
          //  var newName = newTR.insertCell(2);
           // newName.innerHTML = "<a href='#' onclick='deleteThisRow(this);' align='center'>删&nbsp;&nbsp;除</a>";
	}
	
	//删除去表头以外的所有行
	function deleteRows(){
		var ks=document.getElementById("nextTable");
		var rows=ks.rows.length;
		for(var i=1;i<=rows;){
			ks.deleteRow(i);
			--rows;
		}
	}
	
	function onChangeData(){
		deleteRows();
	}
	
	function deleteThisRow(obj){
		var rowIndex=obj.parentNode.parentNode.rowIndex;
		var ks=document.getElementById("nextTable");
		ks.deleteRow(rowIndex);
		
	}
	//选择经销商可以维护时显示新增行
	function doCusChange(value){
		if(value==10041002){
			document.getElementById("addBtn").style.display="inline";
		}else if(value==10041001){
			document.getElementById("addBtn").style.display="none";
		}
	}
	function setTableBtn(){
	 if(document.getElementById("ifDealer").value=='10041001'){
	 	document.getElementById("addBtn").style.display="none";
	 }
	}
	//状态 下拉列表
	function loadStatus(obj) {
		document.getElementById("status").value = obj.getAttribute("TREE_ID");
	}
	//是否经销商可以维护 下拉列表
	function loadIf(obj) {
		document.getElementById("ifDealer").value = obj.getAttribute("TREE_ID");
	}
	