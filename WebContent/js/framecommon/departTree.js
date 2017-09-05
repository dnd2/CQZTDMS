/*
 * CreateDate : 2009-10-19
 * CreateBy   : ChenLiang
 * Comment    : 经销商选择树
 * ModifiedBy : andy.ten@tom.com
 * Comment    : 修改组织树
 */
	
 var myDealerEffects;
 
 var tree_root_id = {"tree_root_id" : ""};
 var subStr = "funlist";
 var pardId = "pardId";
 var addNodeId;
 var panw,panh;
 var sIsMulti = "false"; //false:单选;true:多选
 var sOrgId;
 var ht;
 function showDepartPan(isMulti ,orgId) 
 {
 	if(isMulti)
 	   sIsMulti = isMulti;
 	if(orgId)
 	   sOrgId = orgId;
 	$('tree_root_id').value = "";
 	if(getIEV() <= 6) 
 	{
 		if($('GENDER') != null) 
 		{
	 		$('GENDER').setStyle("visibility","hidden");
	 	}
	 	if($('USER_STATUS') != null) 
	 	{
	 		$('USER_STATUS').setStyle("visibility","hidden");
	 	}
	 	if($('POSE_STATUS') != null) 
	 	{
	 		$('POSE_STATUS').setStyle("visibility","hidden");
	 	}
 	}
	$('pan').setStyle("top",$('DEALER_NAME').getCoordinates().top);
	$('pan').setStyle("left",$('DEALER_NAME').getCoordinates().left);
	
	myDealerEffects.start({
	    'opacity': [0,1],
	    'width' : [0,panw],
	    'height' : [0,panh]
	});
	
	$('myquery').setStyle("top",-1);
	$('myquery').setStyle("left",-1);
	$('dtree').setStyle("top",29);
	$('dtree').setStyle("left",-1);
	$('drlv').setStyle("top",29);
	$('drlv').setStyle("left",$('dtree').getStyle('width').toInt());
	createOrgTree(sIsMulti ,sOrgId);
	getDrl(1 ,sIsMulti ,sOrgId);
 }
 
 var tree_script;
 function createOrgTree() {
	 tree_script = new Element('script');
	 tree_script.setText(" a = new dTree('a','dtree','true','true');");
 	 tree_script.injectInside($('dtree'));
	 
	a.config.closeSameLevel=false;
	//a.config.myfun="dealerPos";
	a.config.folderLinks=true;
	a.delegate=function (id)
	{
		addNodeId = a.aNodes[id].id;
	    var nodeID = a.aNodes[id].id;
//	    $('tree_root_id').value = nodeID;
//	    sendAjax(tree_url,createNode,'fm');
	    for(var n=0; n<a.aNodes.length; n++) {
	    	if(a.aNodes[n].id.contains(addNodeId+"01")) {
	    		return;
	    	}
	    }
	}
	sendAjax(tree_url,createTree,'fm');
	a.closeAll();
 }
 
 function dealerPos(id) {
	var orgid = a.aNodes[id].id;
	var orgname = a.aNodes[id].name;
	$('DEPT_ID').value = orgid;
	getDrl(1);
 }
 
 PPPID = "";

 
 function createTree(reobj) {
	var orglistobj = reobj[subStr];
	var parentOrgId,orgName,orgCode,orgId,poseLevel;
	for(var i=0; i<orglistobj.length; i++) {
		orgId = orglistobj[i].ORG_ID;
		orgName = orglistobj[i].ORG_NAME;
		orgCode = orglistobj[i].ORG_CODE;
		parentOrgId = orglistobj[i].PARENT_ORG_ID;
		poseLevel = orglistobj[i].POSE_LEVEL;
		if(parentOrgId == orgId) { //系统根节点
			a.add(orgId,"-1",orgName,poseLevel);
		} else {
			a.add(orgId,parentOrgId,orgName,poseLevel);
			//a.add(orgId+"_",orgId,"loading...","","","",a.icon.loading,"","");
		}
	}
	a.draw();
	a.openAll();
 }

 function createNode(reobj) {
	var orglistobj = reobj[subStr];
	var orgId,parentOrgId,orgName,orgCode;
	a.remove(addNodeId+"_");
	for(var i=0; i<orglistobj.length; i++) {
		orgId = orglistobj[i].orgId;
		orgName = orglistobj[i].orgName;
		orgCode = orglistobj[i].orgCode;
		parentOrgId = orglistobj[i].parentOrgId;
		a.add(orgId,addNodeId,orgName,orgCode);
		//a.add(orgId+"_",orgId,"loading...","","","",a.icon.loading,"","");
	}
	a.draw();
 }
 
 function isCloseDealerTreeDiv(event,obj,treeDivId) {
	var tempa = $(treeDivId).getStyle("width").toInt();
    var tempb = $(treeDivId).getStyle("height").toInt();
    var ex = event.offsetX+($(obj).getLeft()-$('DEALER_NAME').getLeft());
    var ey = event.offsetY+($(obj).getTop()-$('DEALER_NAME').getTop());
    var st = false;
    if(ex > 0 && ex < tempa && ey > 0 && ey < tempb) {
		st = true;
		var df = document.activeElement.name;
		if(df == 'DRLCODE' || df == 'DELSNAME' || df == 'pageInput2') {
		} else {
			$('DRLCODE').focus();
		}
	}
    if(!st) {
   		closePan();
   		if(getIEV() <= 6) {
   			if($('GENDER') != null) {
		 		$('GENDER').setStyle("visibility","visible");
		 	}
		 	if($('USER_STATUS') != null) {
		 		$('USER_STATUS').setStyle("visibility","visible");
		 	}
		 	if($('POSE_STATUS') != null) {
	 			$('POSE_STATUS').setStyle("visibility","visible");
	 		}
   		}
  	}
 }
 
 //获取选中的部门
 function getFunList() {
		var flist = new Array();
		for(var n=0; n<a.aNodes.length; n++) {
			var tid = a.aNodes[n].id;
			var tsrc = $('ckk' + a.obj + a.aNodes[n]._ai).src;
			var tsrc0 = $('i' + a.obj + a.aNodes[n]._ai).src;
			if(tsrc0.contains("/leaf.gif")  && tsrc.contains("/checked.gif")) {
				flist.push(tid);
			}
		}
		return flist;
	}
 
 function getDrl(page ,isMulti ,orgId) 
 {
 	showMask();
 	var vurl = drlurl+(drlurl.lastIndexOf("?") == -1?"?":"&")+"curPage2="+page;
 	if(submitForm('fm2'))
 	{
 		$('FUNS').value = getFunList().toString();
 		sendAjax(vurl,callBack2,'fm2');
 	}else
 	{
 		$("queryBtn2").disabled = "";
 		removeMask();
 	}
 }
 
 var myPage2;
 function callBack2(json) {
 	$('DRLCODE').focus();
	var ps = json.ps;
	if(ps != null && ps.records != null){
		$("_page2").setStyle("display","none");
		$('myGrid2').setStyle("display","");
		new createGrid2(title2,columns2, $("myGrid2"),ps).load();
		myPage2 = new showPages2("myPage2",ps,drlurl);
		myPage2.printHtml();
	}else{
		$("_page2").setStyle("display","");
		$("_page2").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
		$("myPage2").innerHTML = "";
		removeGird('myGrid2');
		$('myGrid2').setStyle("display","none");
	}
	$('loader').setStyle("display","none");
	$("queryBtn2").disabled = "";
 }
 
 var title2 = "";
	
 var columns2 = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "<input type = \"checkBox\" id = \"leftGet\" onclick = \"leftAllClick();\" />", dataIndex: 'USER_ID', align:'center',width: '7%' ,renderer:seled},
				{header: "姓名", dataIndex: 'NAME', align:'center'},
				{header: "职位类型", dataIndex: 'POSE_TYPE', align:'center',renderer:getPostType}
//				,
//				{header: "职位名称", dataIndex: 'POSE_NAME', align:'center'}
		      ];
 
 function getPostType(value,meta,record){
	 return getItemValue(value);
 }
 
 function seled(value,meta,record) 
 {
 	//added by andy.ten@tom.com 增加多选
 	if(sIsMulti == "false")
 		return "<input type='checkbox' name='userIds' id='userIds' value='"+record.data.USER_ID+"_"+record.data.NAME+"_"+record.data.POSE_CODE+"_"+record.data.POSE_NAME+"_"+getItemValue(record.data.POSE_TYPE)+"'/>";
    else
    	return "<input type='checkbox' name='userIds' id='userIds' value='"+record.data.USER_ID+"_"+record.data.NAME+"_"+record.data.POSE_CODE+"_"+record.data.POSE_NAME+"_"+getItemValue(record.data.POSE_TYPE)+"'/>";
    
 }
 
 /**
  * added by andy.ten@tom.com 多选
  * 预留支持分页多选功能
  */
 var dataArrMulti = new Array();
 function multiSelect(obj) 
 {
 	var userId = $(obj).value.split("_")[0];
 	if($(obj).checked == true){
 		//不存在就增加
 		if(isExistUserID(userId) == false){
 			addUserId(obj);
 		}
 	}else{
 		//移除
 		removeUserId(userId);
 	}
 	
 }
 
 //是否已经存在UserId
 function isExistUserID(userId){
	 var isExistUser = false;
	 for(var i=0;i<dataArrMulti.length;i++) {
		 if(dataArrMulti[i][0] == userId){
			 isExistUser = true;
			 break;
		 }
	 }
	 return isExistUser;
 }
 //增加UserId
 function addUserId(obj){
	 var tempArr = $(obj).value.split("_");
	 dataArrMulti.push(tempArr);
 }
 
 //移除存在的UserID
 function removeUserId(userId){
	 for(var i=0;i<dataArrMulti.length;i++) {
		 if(dataArrMulti[i][0] == userId){
			 dataArrMulti.splice(i,1);
			 break;
		 }
	 }
 }
 
 //将数据展示在主页面
 function chooseConfim() {
		parentContainer.initOemUserList(dataArrMulti);
		_hide();
 }
 
 //将左边的chackBox全选，或全不选
 function leftAllClick()
 {
 	var tab = document.getElementById("myTable2");
 	
 	if(tab.rows.length >1)
 	{
 		for(var i=1; i < tab.rows.length; i++)
 		{
 			var checkObj = tab.rows[i].cells[1].firstChild;
 			if(document.getElementById("leftGet").checked == true){
 	 			checkObj.checked = true;
 			}else{
 				checkObj.checked = false;
 			}
 		}
 		
 		
 	}
 }

//将右边的chackBox全选，或全不选
 function rightAllClick()
 {
	 var tab = document.getElementById("tbody2");
	 	if(tab.rows.length >0)
	 	{
	 		for(var i=0; i < tab.rows.length; i++)
	 		{	
	 			var checkObj = tab.rows[i].cells[1].firstChild;
	 			if(document.getElementById("rightGet").checked == true){
	 	 			checkObj.checked = true;
	 			}else{
	 				checkObj.checked = false;
	 			}
	 		}
	 	}
 }
 
 function addSelect(){
	 var tab = document.getElementById("myTable2");
	 	if(tab.rows.length >1)
	 	{
	 		for(var i=1; i < tab.rows.length; i++)
	 		{
	 			var checkObj = tab.rows[i].cells[1].firstChild;
	 			if(checkObj.checked == true){
	 				var userId = $(checkObj).value.split("_")[0];
	 				if(isExistUserID(userId) == false){
	 					addUserId(checkObj);
	 				}
	 				
	 			}
	 		}
	 		addUserList(dataArrMulti);
	 	}
 }
 
 //移除右边的选中的
 function delSelect(){
	 var tab = document.getElementById("tbody2");
	 	if(tab.rows.length >0)
	 	{
	 		for(var i=0; i < tab.rows.length; i++)
	 		{
	 			var checkObj = tab.rows[i].cells[1].firstChild;
	 			if(checkObj.checked == true){
	 				var userId = $(checkObj).value;
	 				removeUserId(userId);
	 			}
	 		}
	 		addUserList(dataArrMulti);
	 	}
}

 function addUserList(dataArray) {
 	var tbody1 = document.getElementById("tbody2");
 	
 	var __index = 1;
 	var l = tbody1.rows.length;
 	for(var i=0;i<l;i++) {
 		tbody1.deleteRow();
 	}
 	if(dataArray != 'undefined') {
 		for(var i=0;i<dataArray.length;i++) {
 			var tr = document.createElement("tr");
 			tr.className = 'table_list_row2';
 			var td = document.createElement("td");
 			td.innerHTML = __index++;
 			tr.appendChild(td);
 			
 			td = document.createElement("td");
// 			td.innerHTML = "<input name = \"userList\" type=\"checkBox\" value = \""+dataArray[i]+"\" onclick=\"removeUserTR(this,'"+dataArray[i][0]+"')\" />";
 			td.innerHTML = "<input name = \"userList\" type=\"checkBox\" value = \""+dataArray[i][0]+"\" />";
 			tr.appendChild(td);
 			
 			td = document.createElement("td");
 			td.innerHTML = dataArray[i][1] + "<input type='hidden' value='"+dataArray[i][0]+"'/>";
 			tr.appendChild(td);
 			
 			td = document.createElement("td");
 			td.innerHTML = dataArray[i][4];
 			tr.appendChild(td);
 			
// 			td = document.createElement("td");
// 			td.innerHTML = dataArray[i][3];
// 			td.align = 'left';
// 			tr.appendChild(td);
 			
 			tbody1.appendChild(tr);
 		}
 	}
 }

 function removeUserTR(obj,userId) {
 	var s = obj.parentElement.parentElement.parentElement;
 	s.removeChild(obj.parentElement.parentElement);
 	
 	removeUserId(userId);
 }


 
 function closePan() {
	if($('pan').getStyle("opacity") < 1) {
		return ;
	}
	myDealerEffects.start({
	    'opacity': [1,0],
	    'width' : [panw,0],
	    'height' : [panh,0]
	});
	
 }
 
	function removeGird(id){
		$(id).innerHTML = '';
	}

	function createGrid2(title, columns, cnt, ps){
		createGrid2.backColor = "#FDFDFD";	
		createGrid2.hoverColor = "#EEEEEE";
		createGrid2.clickColor = "#EEEEEE";

		this.title = title;
		this.columns = columns;
		this.container = cnt;
		this.table;
		this.curRow;
		this.curCell;
		this.curColums;
		this.jsonData = ps.records;
		this.remoteSort = true;
		this.curPage = ps.curPage;
		this.pageSize = ps.pageSize;

		var CurGrid = this;

		this.load = function(){//grid重画模块

			if($('myTable2') != null){
				removeGird(this.container);
			}
			var tbStr = [], dataIndexArr = [], rendererArr = [], cellCnt=[],index,noWrap,colMask;

			tbStr.push("<table id='myTable2' class='table_list'><tr class='table_list_th'>");

			for(var o=0; o< this.columns.length; o++){//列名	

				if(this.columns[o].orderCol != undefined){
					
					if($("orderCol2").value == this.columns[o].orderCol||$("orderCol2").value.split("-")[0]== this.columns[o].orderCol){
						if($("order2").value == '-1'){
							colMask = "descMask";						
						}else if($("order2").value == '1'){
							colMask = "ascMask";
						}
					}else{
						colMask = "sortMask";
					}
				}else{
					colMask = "noSort";
				}

				if(this.columns[o].width == undefined){
					tbStr.push("<th class='"+ colMask + "'>"+ this.columns[o].header+"</th>");
				}else{			
					tbStr.push("<th class='"+ colMask + "'" +"width="+ this.columns[o].width +">"+ this.columns[o].header+"</th>");
				}				
				dataIndexArr.push(this.columns[o].dataIndex);//记录dataIndex				
				rendererArr.push(this.columns[o].renderer);  //记录renderer
			}

			tbStr.push("</tr>");

			for(var i=0; i< this.jsonData.length;i++){//			
				/*if(i%2 != 0){tbStr.push("<tr class='table_list_row1'>");}else{tbStr.push("<tr class='table_list_row2' >");}*/			
				tbStr.push("<tr class='table_list_row1'>");

				for(var j=0;j<dataIndexArr.length;j++){	
					
					cellCnt = this.jsonData[i][dataIndexArr[j]];//根据dataIndex显示后台数据
					
					if(cellCnt == null || cellCnt == undefined){
						cellCnt ='';
					}
					//alert(this.jsonData[i][dataIndexArr[j]] == undefined);
					
					if(typeof(rendererArr[j])=='function'){//列名有renderer属性
						var __data__ = {};
						__data__.data = this.jsonData[i];
						cellCnt = this.columns[j].renderer(cellCnt,{},__data__);//显示renderer函数，传值		
					}
					
					if(this.columns[j].style == undefined){
						styleV = '';
					}else{
						styleV = this.columns[j].style;
					}
					tbStr.push("<td style='"+ styleV +"'>" + cellCnt + "</td>");
				}				
				tbStr.push("</tr>");
				
			}
					
			tbStr.push("</table>");
			this.container.innerHTML = tbStr.join("");
			this.table = this.container.firstChild;

			if(this.title != null){//表格标题
				var x = $('myTable2').createCaption();
				x.innerHTML = "<span class='navi'>&nbsp;</span>"+this.title;
			}

			/** 设置单元格  **/
			for(var r=1; r<this.table.rows.length;r++){
	
				if(dataIndexArr[0] == undefined || rendererArr[0] == "function getIndex(){}"){//序号判断
				
					if(this.curPage == 1){//计算序号
						index = r;
					}else{
						index = parseInt(this.curPage-1)*this.pageSize + r;
					}			
					this.table.rows[r].cells[0].innerHTML = index; 
					this.table.rows[r].cells[0].style.textAlign = 'center';//序号单元格居中			
				}
				
				this.table.rows[r].onmouseover = function(){ this.style.backgroundColor = createGrid2.hoverColor; }
				this.table.rows[r].onmouseout = function(){ 
					if(CurGrid.curRow!=this) this.style.backgroundColor = createGrid2.backColor; 
					else this.style.backgroundColor = createGrid2.clickColor;
				}
	
				for(var c=0;c<this.table.rows[r].cells.length;c++){
					this.table.rows[r].cells[c].onclick = function(){
						if(CurGrid.curRow) CurGrid.curRow.style.backgroundColor = createGrid2.backColor;
						CurGrid.curRow = this.parentNode;
						this.parentNode.style.backgroundColor = createGrid2.clickColor;
					}
	
				}
			}

			for(var g=0; g<this.table.rows[0].cells.length;g++){
				this.table.rows[0].cells[0].style.textAlign = 'center';//序号列居中
				if(this.columns[g].orderCol != undefined){
					this.table.rows[0].cells[g].onclick = function(){

						var _order = 1;
						if(!$("queryBtn2").disabled){//亮
							//if(CurGrid.table.rows[0].cells[this.cellIndex].innerHTML.lastIndexOf('▲')!= -1){
							if(CurGrid.table.rows[0].cells[this.cellIndex].className == "ascMask"){
								_order = '-1';
							}								
						}else{
							if($("orderCol2").value != this.cellIndex){return false;}
							if(CurGrid.table.rows[0].cells[this.cellIndex].className == "ascMask"){
								_order = '1';
							}
							if(CurGrid.table.rows[0].cells[this.cellIndex].className == "descMask"){
								_order = '-1';
							}
						}
						CurGrid.sort(this.cellIndex, CurGrid.columns[this.cellIndex].orderCol,_order,CurGrid.columns[this.cellIndex].orderType);	

					}
				}
			}
			
		}
	
		this.sort = function(n, orderCol,order,orderType){  //排序 n-列 type-升降序
		
			if(typeof(this.remoteSort) == 'undefined' || this.remoteSort == false){//当前页排序			
				this.jsonData = this.jsonData.sort(function(x,y){
					if (x[n]>y[n]){return type;}else if(x[n]<y[n]){return -type;}else{return 0;}});
			}else{//远程排序
				if($('myTable2') != null){
					removeGird(this.container);
				}
				myRemoteSort(orderCol,order,orderType);
			}
			this.load();
		}
	}
	
	//远程排序接口
	function myRemoteSort2(orderCol,order,orderType){
		//排序类型 0:不排 -1:降序 1:升序
		if(orderType!=undefined&&orderType!=""){
			$("orderCol2").value = orderCol+"-"+orderType;
		}else{
			$("orderCol2").value = orderCol;
		}
		$("order2").value = order;

		if(!$("queryBtn2").disabled){//亮
			getDrl(1);
		}
		//alert("当前列="+col+" | "+"排序类型="+order +" | "+ "此为排序接口，远程请求在此写！");
	}

	function getIndex2(){}
/*============================================分页=================================================*/

	function showPages2(name,ps,url) { //初始化属性
		this.name = name; //对象名称
		this.url = url; //action
		this.page = ps.curPage; //当前页数
		this.pageCount = ps.totalPages; //总页数
		this.totalRecords = ps.totalRecords; //总记录数
	}
	
	showPages2.prototype.getPage = function(){ //丛url获得当前页数,如果变量重复只获取最后一个
		var args = location.search;
		var reg = new RegExp('[\?&]?' + this.argName + '=([^&]*)[&$]?', 'gi');
		var chk = args.match(reg);
		this.page = RegExp.$1;
	}
	showPages2.prototype.checkPages = function(){ //进行当前页数和总页数的验证
		if (isNaN(parseInt(this.page))) this.page = 1;
		if (isNaN(parseInt(this.pageCount))) this.pageCount = 1;
		if (this.page < 1) this.page = 1;
		if (this.pageCount < 1) this.pageCount = 1;
		if (this.page > this.pageCount) this.page = this.pageCount;
		this.page = parseInt(this.page);
		this.pageCount = parseInt(this.pageCount);
	}
	showPages2.prototype.createHtml = function(){ //生成html代码
		var strHtml = '', prevPage = this.page - 1, nextPage = this.page + 1;
			strHtml += '<span class="count">总条数: ' + this.totalRecords + '</span>';
			strHtml += '<span class="number">';
		
			if (prevPage < 1) {
			  // strHtml += '<span title="First Page">&#171;</span>';
			} else {
			//strHtml += '<span title="First Page"><a href="javascript:' + this.name + '.toPage(1);">&#171;</a></span>';
			strHtml += '<span title="Prev Page"><a href="javascript:' + this.name + '.toPage(' + prevPage + ');">上一页</a></span>';
			}
			//if (this.page != 1)
			if (this.page < 5 && this.page > 1) strHtml += '<span title="Page 1"><a href="javascript:' + this.name + '.toPage(1);">1</a></span>';
			if (this.page >= 5) strHtml += '<span title="Page 1"><a href="javascript:' + this.name + '.toPage(1);">1..</a></span>';//strHtml += '<span style="border:none; background:#FFF;"><a href="javascript:void(0)">1...</a></span>';
			if (this.pageCount > this.page + 2) {
			var endPage = this.page + 2;
			} else {
			var endPage = this.pageCount;
			}
			
			for (var i = this.page - 2; i <= endPage; i++) {
			if (i > 0) {
			  if (i == this.page) {
				 strHtml += '<span title="Page ' + i + '"><a href="javascript:void(0)" style="color:#FF9900;background:#DDD;">' + i + '</a></span>';
			  } else {
				if (i != 1 && i != this.pageCount) {
				 strHtml += '<span title="Page ' + i + '"><a href="javascript:' + this.name + '.toPage(' + i + ');">' + i + '</a></span>';
				}
			  }
			}
			}
			if (this.page + 3 <= this.pageCount) strHtml += '<span title="Page ' + this.pageCount + '"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">..' + this.pageCount + '</a></span>';//strHtml += '<span style="border:none; background:#FFF;"><a href="javascript:void(0)">..</a></span>';
			if (this.page + 3 > this.pageCount && this.page != this.pageCount) strHtml += '<span title="Page ' + this.pageCount + '"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">' + this.pageCount + '</a></span>';
			if (nextPage > this.pageCount) {
			//strHtml += '<span title="Next Page">&#8250;</span>';//strHtml += '<span title="Last Page">&#187;</span>';
			} else {
			strHtml += '<span title="Next Page"><a href="javascript:' + this.name + '.toPage(' + nextPage + ');">下一页</a></span>';
			// strHtml += '<span title="Last Page"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">&#187;</a></span>';
			}	  
			
			if (this.pageCount < 1) {
			strHtml += '<input type="text" name="toPage" value="No Pages" class="mini_txt" disabled="disabled" style="margin-left:-10px;">';
			strHtml += '<input type="button" name="go" value="GO" class="mini_btn" disabled="disabled">';
			} else {
			strHtml += '<input type="text" name="pageInput2" id="pageInput2" value="' + this.page + '" class="mini_txt" title="Input page" onkeydown="return ' + this.name + '.formatInputPage(event);" style="margin-left:-10px;">&nbsp;';
			strHtml += '<input type="button" name="go" value="Go" class="mini_btn" onclick="' + this.name + '.toPage(document.getElementById(\'pageInput2' + '\').value);">';
			}
			strHtml += '</div>';
		
		return strHtml;
	}

	showPages2.prototype.createUrl = function (page) { //生成页面跳转url
		if (isNaN(parseInt(page))) page = 1;
		if (page < 1) page = 1;
		if (page > this.pageCount) page = this.pageCount;
		var url = location.protocol + '//' + location.host + location.pathname;
		var args = location.search;
		var reg = new RegExp('([\?&]?)' + this.argName + '=[^&]*[&$]?', 'gi');
		args = args.replace(reg,'$1');
		if (args == '' || args == null) {
		args += '?' + this.argName + '=' + page;
		} else if (args.substr(args.length - 1,1) == '?' || args.substr(args.length - 1,1) == '&') {
		  args += this.argName + '=' + page;
		} else {
		  args += '&' + this.argName + '=' + page;
		}
		return url + args;
	}

	showPages2.prototype.toPage = function(page){ //页面跳转
	  /*var turnTo = 1;
	  if (typeof(page) == 'object') {
		turnTo = page.options[page.selectedIndex].value;
	  } else {
		turnTo = page;
	  }
	  self.location.href = this.createUrl(turnTo);*/
	  //removeGird('myGrid');
	  if(page>=1 && page<=this.pageCount)
	  	getDrl(page);
	  //makeFormCall(this.url+"&curPage="+page,callBack,'fm');
	}
	
	showPages2.prototype.printHtml = function(){ //显示html代码
  		//this.getPage();	
		//this.checkPages();
		//var pageBox = this.name;
		$(this.name).innerHTML = this.createHtml();
		//callBack();
	}

	showPages2.prototype.formatInputPage = function(evt){ //限定输入页数格式
		var evt=evt?evt:(window.event?window.event:null);
		if(evt.keyCode == 13 && $("pageInput2").value>=1 && $("pageInput2").value<=this.pageCount){
			getDrl($("pageInput2").value);	
		}
		/*var ie = navigator.appName=="Microsoft Internet Explorer"?true:false;
		if(!ie) var key = e.which;
		else var key = event.keyCode;
		if (key == 8 || key == 46 || (key >= 48 && key <= 57)) return true;
		return false;*/
	 }

	 
	 /*============================================查询效果 部分===========================================*/
		function disableBtn2(obj){
			obj.disabled = true;
			obj.style.border = '1px solid #999';
			obj.style.background = '#EEE';
			obj.style.color = '#999';
			showMask();
		}
		function useableBtn2(obj){
			obj.disabled = false;
			obj.style.border = '1px solid #5E7692';
			obj.style.background = '#EEF0FC';
			obj.style.color = '#1E3988';
			removeMask();
		}
		
		function showMask2(){
			if($('loader')){
				var screenW = document.viewport.getWidth()/2;	
				$('loader').style.left = (screenW-20) + 'px';
				$('loader').innerHTML = ' 正在载入中... ';
				$('loader').show();
			}
		}
		
		function removeMask2(){
			if($('loader'))
				$('loader').hide();
		}
		
		function getIndex(){}