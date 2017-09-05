	var context=null;
	var myPage;
	var url = null;
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex},
				{header: "机构代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "机构名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
				{header: "上级字典名称", dataIndex: 'TYPE_NAME', align:'center'},
				{header: "本级字典代码", dataIndex: 'CODE_ID', align:'center'},
				{header: "本级字典名称", dataIndex: 'CODE_DESC', align:'center'},
				{header: "级数", dataIndex: 'CODE_LEVEL', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作", dataIndex: 'CODE_ID', align:'center',renderer:dataUpdateInit}
		      ];
	function doInit(){
		context=$('curPaths').value
		url=context+"/crm/data/DataManage/dataQueryList.json?COMMAND=1";
		__extQuery__(1);
	} 
	
	function dataUpdateInit(value,meta,record){
		var code_id=record.data.CODE_ID;
		//判断是否经销商可以维护
	//	var ifDealer=record.data.IF_DEALER;
		var str="";
	//	if(ifDealer=='10041002'){
			str+="<a href='#' onclick='dataUpdate(\""+ code_id +"\")'>[修改]</a>/<a href='#' onclick='dataSelect(\""+ code_id +"\")'>[查看]</a>";
	//	}else{
	//		str+="<a href='#' onclick='dataSelect(\""+ code_id +"\")'>[查看]</a>";
	//	}
		return String.format(str) ;
	}
	
	function dataUpdate(codeId){
		var urls = context+"/crm/data/DataManage/dataModifyInit.do?codeId="+codeId;
		location.href=urls;
	}
	
	function dataSelect(codeId){
		var urls = context+"/crm/data/DataManage/dataSelectInit.do?codeId="+codeId;
		location.href=urls;
	}
	//点击保存时执行的方法
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
			alert("子类名称不能为空！！！");
			return;
		}
		var context=$('curPaths').value
		var urls=context+"/crm/data/DataManage/saveData.json";
		makeFormCall(urls, saveResult, "fm") ;
	}
	//保存的回调函数
	function saveResult(json){
		if(json.flag==1){
			alert("操作成功！！");
			location.href=context+"/crm/data/DataManage/doInit.do";
		}else if(json.flag==2){
			alert("存在经销商维护了下级，无法修改为车厂维护！！");
		}else if(json.flag==3){
			alert("存在车厂维护了下级，无法修改为经销商维护！！");
		}else{
			alert("操作失败！！");
		}
	}
	
	
	//操作table的相关方法
	function tableBtn(){
			var ks=document.getElementById("nextTable");
			var rows=ks.rows.length;
			var newTR = document.getElementById("nextTable").insertRow(rows);
			 var newNameTD = newTR.insertCell(0);
			 newNameTD.style.cssText="BORDER:   black   1px   solid";
			 newNameTD.innerHTML = 'SUZUKI';
			 newNameTD = newTR.insertCell(1);
			 newNameTD.style.cssText="BORDER:   black   1px   solid";
			 newNameTD.innerHTML = '重庆长安铃木汽车有限公司';
             newNameTD = newTR.insertCell(2);
             newNameTD.style.cssText="BORDER:   black   1px   solid";
            var parCode="&nbsp;<input type='hidden' name='nextCodeId' value='' />";
            newNameTD.innerHTML = parCode;
            newNameTD = newTR.insertCell(3);
            newNameTD.style.cssText="BORDER:   black   1px   solid";
            newNameTD.innerHTML = "<input name='nextCodeDesc' id='nextCodeDesc' type='text'  style='width:100%'/>";
           // var newName = newTR.insertCell(4);
            //newName.innerHTML = "<a href='#' onclick='deleteThisRow(this);' align='center'>删&nbsp;&nbsp;除</a>";
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
	//删除当前行
	function deleteThisRow(obj){
		var rowIndex=obj.parentNode.parentNode.rowIndex;
		var ks=document.getElementById("nextTable");
		ks.deleteRow(rowIndex);
		
	}
	//当修改经销商是否可以维护下级时执行的方法
	function doCusChange(value){
		if(value==10041002){
			document.getElementById("addBtn").style.display="inline";
		}else if(value==10041001){
			document.getElementById("addBtn").style.display="none";
		}
	}
	//当为经销商维护时不显示新增按钮
	function setTableBtn(){
		 if(document.getElementById("ifDealer").value=='10041001'){
		 	document.getElementById("addBtn").style.display="none";
		 }
	}
	function loadStatus(obj){
		$("status").value=obj.getAttribute("TREE_ID");
	}
	function loadIfDealer(obj){
		$("ifDealer").value=obj.getAttribute("TREE_ID");
	}
	function loadIfVisible(obj){
		$("ifVisible").value=obj.getAttribute("TREE_ID");
	}
	