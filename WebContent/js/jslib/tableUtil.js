// 复选id分隔符
var checkSep = ",";
// ===========调整表格高度 end==========================
var minHeight = 200;
var scrollBarHeight = 20;
var defaultGridName = 'myGrid';
var defaultTableName = 'myTable';
// 创建记录表格高度的隐藏域
function createTableHeightNode() {
	var heightNode = document.createElement('input');
	heightNode.setAttribute('type', 'hidden');
	heightNode.setAttribute('id', tableInitHeight);
	document.body.appendChild(heightNode);

	var preHeightNode = document.createElement('input');
	preHeightNode.setAttribute('type', 'hidden');
	preHeightNode.setAttribute('id', tablePreHeight);
	document.body.appendChild(preHeightNode);
}

function setTableHeight(tableHeight) {
	if ($(tableInitHeight)) {

	} else {
		createTableHeightNode();
	}
	$(tableInitHeight).value = tableHeight ? tableHeight : 0;
}

function createChangeHeightNode(myGridName){
	var strHtml = '';
	strHtml += '<span class="changeHeightSpan">';
	strHtml += '<span class="count">表格缩放:'
		+'&nbsp;&nbsp;'
		+'<span class="changeHeightButtonLabel" onclick="addHeight('+(myGridName?myGridName:'')+');">+'
		+'</span>'
		+'&nbsp;&nbsp;'
		+'<span class="changeHeightButtonLabel" onclick="subHeight('+(myGridName?myGridName:'')+');">-'
		+'</span>'
		+'&nbsp;&nbsp;'
		+'</span>';
	strHtml += '&nbsp;&nbsp;&nbsp;&nbsp;'
			+'</span>';
	return strHtml;
}
//===========调整表格高度 end==========================

// ====================调整列宽start by chenyu=======================
function MouseDownToResize(obj) {
	setTableLayoutToFixed();
	obj.mouseDownX = event.clientX;
	obj.pareneTdW = obj.parentNode.offsetWidth;
	obj.pareneTableW = myTable.offsetWidth;
	obj.setCapture();
}
function MouseMoveToResize(obj,tableId) {
	if (!obj.mouseDownX){
		return false;
	}
	var newWidth = obj.pareneTdW * 1 + event.clientX * 1 - obj.mouseDownX;
	if (newWidth > 10) {
		var myTable = document.getElementById(tableId?tableId:defaultTableName);
		obj.parentElement.style.width = newWidth;
		myTable.style.width = obj.pareneTdW * 1 + event.clientX * 1
				- obj.mouseDownX;
	}
}
function MouseUpToResize(obj) {
	obj.releaseCapture();
	obj.mouseDownX = 0;
}
function setTableLayoutToFixed(myGridName) {
	var gridName = myGridName?myGridName:defaultGridName;
	var mygridNode = document.getElementById(defaultGridName);
	var tableName = getTableName(gridName);
	var myTable = $(tableName);
	if (myTable.style.tableLayout == 'fixed'){
		return;
	}
	var nWidth = 0;
	var headerTr = myTable.rows[0];
	var sys = getBrowserInfo();
	var addWidth = 17;
	if(sys.browser=='msie'){
		
	} else if(sys.browser=='chrome'){
		addWidth = 25;
	} else if(sys.browser=='safari'){
		addWidth = 30;
	}
	for (var i = 0; i < headerTr.cells.length; i++) {
		var cellText = getCellValue(headerTr.cells[i]);
		var tLength = 0;
		if (cellText) {
			tLength = tLength-0 + cellText.length*15-0;
			if (!isWithoutHeader(cellText,i)) {
				tLength = tLength-0+addWidth;
			}
		}
		headerTr.cells[i].styleOffsetWidth = 
			parseInt(headerTr.cells[i].offsetWidth)>parseInt(tLength)?parseInt(headerTr.cells[i].offsetWidth):parseInt(tLength);

		nWidth += parseInt(headerTr.cells[i].styleOffsetWidth);
	}

	for (var i = 0; i < headerTr.cells.length; i++) {
		headerTr.cells[i].style.width = headerTr.cells[i].styleOffsetWidth+'px';
	}
	var tWidth = (parseInt(mygridNode.scrollWidth)>parseInt(nWidth))?parseInt(mygridNode.scrollWidth):parseInt(nWidth);
	myTable.style.width = tWidth+'px';
	myTable.style.tableLayout = 'fixed';
}

/**
 * 是否需要调整宽度
 * @param myGridName
 * @returns {Boolean}
 */
function isNeedAdjustWidth(myGridName) {
	var gridName = myGridName ? myGridName : defaultGridName;
	var mygridNode = document.getElementById(defaultGridName);
	var tableName = getTableName(gridName);
	var myTable = $(tableName);
	if (myTable.getAttribute('adjustEdWidth')) {
		return false;
	}
	return true;
}

/**
 * 设置已调整宽度
 * @param myGridName
 */
function setAdjustedWidth(myGridName){
	var gridName = myGridName ? myGridName : defaultGridName;
	var mygridNode = document.getElementById(defaultGridName);
	var tableName = getTableName(gridName);
	var myTable = $(tableName);
	myTable.setAttribute('adjustEdWidth',true);
}

function createResizeNode(){
	var resizeNodeStr = "<span class='resizeColumnWidthClass' "
		+ " onmousedown='MouseDownToResize(this);' "
		+ " onmousemove='MouseMoveToResize(this);' "
		+ " onmouseup='MouseUpToResize(this);' >"
		+ "<img src='"+globalContextPath+"/_none.gif' height='18'>"
		+ "</span>";
	return resizeNodeStr;
}
function createSortNode(){
	// 交换列标记
	var sortNodeStr = "<span class='"+sortTableClass+"' "
		+ " onclick='sortTable(this)' "
		+ " >"
		+ "</span>";
	return sortNodeStr;
}
// ====================调整列宽end by chenyu=======================

// ====================初始化checkbox选中start by chenyu ===========
function initCheckboxChecked(checkVals,columns){
	var oTable = document.getElementById(dragTableId);
	if(oTable){
		var rows = oTable.rows;
		// 先找出checkBox的列
		var cbCol = -1;
		if(rows&&rows.length>0){
			var thRow = rows[0];
			var cells = thRow.cells;
			var cols = cells.length;
			for(var i=0;i<cols;i++){
				var cell = cells[i];
				var ih = cell.innerHTML;
				if(cell&&hasSelectAll(ih)){
					cbCol = i;
					break;
				}
			}
		}
		if(cbCol == -1){
			return;
		}
		var hideCheckedNode = document.getElementById(hideCheckedId);
		if(hideCheckedNode){
			checkVals = hideCheckedNode.value;
			refreshCheckedNum();
		}
		if(checkVals){
			checkVals = checkVals.split(checkSep);
		} else {
			checkVals = '';
		}
		var valLen = checkVals.length;
		// 找到列后编列每一行,校验复选框列是否被选中了
		var rowLen = rows.length;
		for(var i = 1;i<rowLen;i++){
			var cell = rows[i].cells[cbCol];
			var checkBox = getCellByType(cell,'checkbox');
			// 可用且没有click事件的才绑定事件
			// 根据使用情况确认是否修改为增加事件
			if(checkBox){
				if('on'!=checkBox.value&&!checkBox.onclick){
					// 给checkbox绑定事件
					checkBox.onclick=function() {
						checkboxClick(this);
				    };
				}
				// 确认checkbox是否应该选中
				var value = checkBox.value;
				for(var j=0;j<valLen;j++){
					if(value==checkVals[j]){
						checkBox.checked = true;
						break;
					}
				}
			}
		}
		
	}
	
}
//====================初始化checkbox选中end by chenyu ===========

//====================复选框功能start by chenyu ===========
// 查找指定的元素
function getCellByType(_node,_nodeType){
	var _r = null;
	if(_node){
		// 如果是文本节点
		if (_node.nodeType == 3) {
			_r = null;
		}
		// 如果是复选框
		else if (_node.nodeType == 1 
				&& (_node.tagName.toLowerCase() == _nodeType.toLowerCase()
					|| _node.getAttribute('type') 
					&& _node.getAttribute('type').toLowerCase()==_nodeType.toLowerCase())) {
			_r = _node;
		}
		// 如果是元素节点,且还有子节点
		else if (_node.nodeType == 1 && _node.childNodes.length > 0) {
			_r = getCellByType(_node.firstChild,_nodeType);
		} else {
			_r = null;
		}
	}
	return _r;
}

// 查看是否有全选的复选框
function hasSelectAll(_nodeHtml){
	var _r = false;
	if(_nodeHtml){
		var _nodehtml = _nodeHtml.toLowerCase();
		if(_nodehtml.indexOf('checkbox')>-1
				&&_nodehtml.indexOf('selectall')>-1
				||_nodehtml.indexOf('选择')>-1){
			_r = true;
		}
	}
	return _r;
}

// 点击事件
function checkboxClick(checkObj){
	if(checkObj){
		if(checkObj.checked==true){
			saveCheckboxValue(checkObj);
		} else {
			removeCheckboxValue(checkObj)
		}
	}
	refreshCheckedNum();
}

function createHideCheckedNode(){
	var _nodeStr = "<input type='hidden' id='"
		+ hideCheckedId +"' "
		+ " />";
	_nodeStr += "&nbsp;<label style='color:red;' id='"+checkedNumNodeId+"'>"
		+"</label>";
	return _nodeStr;
}
//====================复选框功能end by chenyu ===========


// =============自定义每页条数功能start by chenyu================================
function createCustomPageSizeNode(){
	var strHtml = '';
	strHtml += '<span class="count">每页</span>';
	strHtml += '<select id="mainPageSize" name="mainPageSize" style="float:left"'
		+ ' onchange="__extQuery__(1)">'
		+ '<option value=5>5</option>'
		+ '<option value=10>10</option>'
		+ '<option value=15>15</option>'
		+ '<option value=30>30</option>'
		+ '<option value=50>50</option>'
		+ '<option value=100>100</option>'
		+ '</select>';
	strHtml += '<span class="count">条</span>';
	return strHtml;
}
// =============自定义每页条数功能end by chenyu==================================




// =============动态设置表格高度start by chenyu============================
/**
 * 动态设置tablediv的高度
 */
function setGridHeight(myGridName){
	var gridName = myGridName?myGridName:defaultGridName;
	var tableName = getTableName(gridName);
	var gridHeight = 0;
	var sys = getBrowserInfo();
	var htmlHeight = document.documentElement.offsetHeight;
	if(sys.browser!='msie'){
		htmlHeight = document.documentElement.clientHeight;
	}
	var gridInitHeight = htmlHeight-$(gridName).offsetTop-50;
	var tableFactHeight = getTableHeight(tableName);
	if(gridInitHeight>tableFactHeight+scrollBarHeight&&isNeedAdjustWidth(myGridName)){
		gridInitHeight = tableFactHeight+scrollBarHeight;
	}
	if(gridInitHeight<minHeight){
		gridInitHeight = minHeight;
	}
	if($(tablePreHeight)&&$(tablePreHeight).value){
		gridHeight = $(tablePreHeight).value;
	} else {
		gridHeight = gridInitHeight;
	}
	$(gridName).style.height = gridHeight + 'px';
	setTableHeight(gridInitHeight);
}

function getTableHeight(myTableName){
	var tableName = myTableName?myTableName:defaultTableName;
	var tbHeight = 0;
	if($(tableName)){
		var rows = $(tableName).rows;
		var rowsLen = rows.length;
		for (i = 0; i < rowsLen; i++) {
			tbHeight += parseInt(rows[i].offsetHeight);
		}
	}
	return tbHeight;
}
function getTableName(gridName){
	var tableName = gridName == defaultGridName ? defaultTableName
			: (defaultTableName + gridName.replace(defaultGridName, ''));
	return tableName;
}
// =============动态设置表格高度end by chenyu==============================
// =============表格缩放功能start by chenyu================================
function addHeight(myGridName){
	changeTableHeight('add',myGridName);
}
function subHeight(myGridName){
	changeTableHeight('sub',myGridName);
}
function changeTableHeight(_type,myGridName){
	var gridName = myGridName?myGridName:defaultGridName;
	var tableName = getTableName(gridName);
	if($(tableInitHeight)&&$(gridName)&&$(tablePreHeight)){
		var srcHeight = $(tableInitHeight).value.replace('px','');
		var curHeight = $(gridName).style.height.replace('px','');
		var finalHeight = 0;
		if('add'==_type){
			finalHeight = $(gridName).scrollHeight;
		}else if('sub'==_type) {
			if(parseInt(curHeight)<=parseInt(srcHeight)){
				return;
			}
			finalHeight = curHeight - srcHeight;
		}
		var tableFactHeight = getTableHeight(tableName);
		if(finalHeight>tableFactHeight+scrollBarHeight){
			finalHeight = tableFactHeight+scrollBarHeight;
		}
		if(finalHeight<srcHeight){
			finalHeight = srcHeight;
		}
		if(finalHeight<minHeight){
			finalHeight = minHeight;
		}
		$(gridName).style.height = finalHeight + 'px';
		$(tablePreHeight).value = $(gridName).style.height.replace('px','');
	}
}
// =============表格缩放功能end by chenyu==================================

/**
 * 获取与指定node平级的前面node的高度和
 */
function getPreHeightByNode(node){
	var preHeight = 0;
	if(node){
		var pNode = node.parentNode;
		if(pNode){
			var children = pNode.childNodes;
			if(children&&children.length>0){
				for(var i = 0;i<children.length;i++){
					var curNode = children[i];
					if(curNode==node){
						break;
					}
					if(curNode.nodeType==1&&curNode.tagName.toLowerCase()!='script'
						&&curNode.tagName.toLowerCase()!='link'
						&&curNode.tagName.toLowerCase()!='meta'
						&&curNode.tagName.toLowerCase()!='style'){
						preHeight = preHeight-0+(curNode.clientHeight==null?0:curNode.clientHeight);
					}
				}
				// 如果父节点是body,不在迭代,否则继续计算上一级前面的高度
				if(pNode.nodeType==1&&pNode.tagName.toLowerCase()=='body'){
					
				} else{
					preHeight = preHeight-0 + getPreHeightByNode(pNode);
				}
			}
		}
	}
	return preHeight;
}
function getBrowserInfo(){
    var Sys = {};
    var ua = navigator.userAgent.toLowerCase();
    var re =/(msie|firefox|chrome|opera|version).*?([\d.]+)/;
    var m = ua.match(re);
    Sys.browser = m[1].replace(/version/, "'safari");
    Sys.ver = m[2];
    return Sys;
}