// ====================交换列start by chenyu=======================
// 例外的列头 
var withoutHeader = new Array();
withoutHeader[0] = '序号';
withoutHeader[1] = '操作';
withoutHeader[2] = '选择';
var withoutHeaderNode = new Array();
withoutHeaderNode[0] = 'checkbox';
var withoutCells ;

/**
 * 
 * @param nodeHtml
 * @param cellNo
 * @returns
 */
function isWithoutHeader(nodeHtml,cellNo){
	var _r = false;
	// 如果列号小于冻结列,视为除外列
	if(withoutCells&&cellNo){
		if(withoutCells>cellNo){
			_r = true;
		}
	} else {
		// 校验是否是除外列头
		for(var idx = 0;idx<withoutHeader.length;idx++){
			wh = withoutHeader[idx];
			if(nodeHtml.toLowerCase().indexOf(wh)==0){
				_r = true;
				break;
			}
		}
		// 校验是否包含除外的Node
		if(!_r){
			for(var idx = 0;idx<withoutHeaderNode.length;idx++){
				wh = withoutHeaderNode[idx];
				if(nodeHtml.toLowerCase().indexOf(wh)>-1){
					_r = true;
					break;
				}
			}
		}
	}
	return _r;
}


//是否能拖动
var dragMoveable = false;
// 表格id
var dragTableId = 'myTable';
// 被拖动列
var dragSrcCell;
var dragSrcCellIndex;
// 拖动源的原颜色
var dragSrcPreColor;
// 拖动源的高显颜色
var dragSrcColor = '#CDCDCD';
// 拖动的目标列
var dragOverCell;
var dragOverCellIndex;
// 拖动的目标的高显颜色
var dragOverColor = '#00FFFF';

// 拖动列头的坐标
var dragedTable_x0;
var dragedTable_y0;
// 鼠标坐标
var dragedTable_x1;
var dragedTable_y1;
// 拖动的div
var dragDivId = 'dragdiv';

var tableScrollLeft = 0;
var docScrollTop = 0;

//交换表中的两列，需要遍历每一行，然后交换每一个元素。
function ChangeColumn(table, srcCell, tagCell) {
	var srcIndex = srcCell.cellIndex;
	var tagIndex = tagCell.cellIndex;
	if (null != srcIndex && undefined != srcIndex 
			&& null != tagIndex	&& undefined != tagIndex) {
		for (var i = 0; i < table.rows.length; i++){
			var row = table.rows[i];
			// swapNode()仅支持IE
			row.cells[srcIndex].swapNode(row.cells[tagIndex]);
		}
	}
}

// 初始化拖动的div
function dragDivInit() {
	dragdiv = document.createElement("div");
	dragdiv.id = dragDivId;
	dragdiv.setAttribute("align","center");
	dragdiv.onselectstart = function() {
		return false
	};
	dragdiv.style.cursor = "hand";
	dragdiv.style.position = "absolute";
	dragdiv.style.border = "0px solid black";
	dragdiv.style.display = "none";
	document.body.appendChild(dragdiv);
}
// 拖动div事件
function movespan(obj) {
	// 显示拖动的div
	showDragDiv();
	
	// 鼠标move事件
	document.onmousemove = function() {
		var tre = event.srcElement;
		if (dragMoveable) {
			var oDiv = document.getElementById(dragDivId);
			var pos = new Array();
			oDiv.style.top = event.clientY - dragedTable_y1 + dragedTable_y0;
			oDiv.style.left = event.clientX - dragedTable_x1 + dragedTable_x0-tableScrollLeft;
			var oTable = document.getElementById(dragTableId);
			var thRow = oTable.rows[0];
			var overCell;
			pos = getPos(thRow.cells[2]);
			// 高显需要被替换的列头
			for (var i = 0; i < thRow.cells.length; i++) {
				overCell = thRow.cells[i];
				if (overCell.tagName.toLowerCase() == "th"&&overCell.getAttribute('moveTh')=='moveTh') {
					pos = getPos(overCell);
					if (event.clientX > pos[1]-tableScrollLeft
							&& event.clientX < pos[1]-0 + overCell.offsetWidth-tableScrollLeft
							&& event.clientY > pos[0]-docScrollTop
							&& event.clientY < pos[0]-0 + overCell.offsetHeight-docScrollTop) {
						if (overCell != dragSrcCell){
							overCell.style.backgroundColor = dragOverColor;
						}
					} else {
						if (overCell != dragSrcCell){
							overCell.style.backgroundColor = dragSrcPreColor;
						}
					}
				}
			}
		}
	}
	
	// 鼠标弹起事件
	document.onmouseup = function() {
		hideDragDiv();
	}
}

// 显示拖动的列头
function showDragDiv() {
	var obj = event.srcElement;
	var pos = new Array();
	//获取过度图层
	var oDiv = document.getElementById(dragDivId);
	if (obj.tagName.toLowerCase() == "th") {
//		obj = obj.parentNode.parentNode;
		obj.style.cursor = "hand";
		obj.style.filter = "alpha(opacity=100)";
		pos = getPos(obj);
		//计算中间过度层位置，赋值
		oDiv.style.width = obj.offsetWidth;
		oDiv.style.height = obj.offsetHeight;
		oDiv.innerHTML = obj.innerHTML;
		oDiv.style.display = "";
		oDiv.setCapture();
		dragedTable_x0 = pos[1];
		dragedTable_y0 = pos[0];
		dragedTable_x1 = event.clientX;
		dragedTable_y1 = event.clientY;
		//记住原td
		dragSrcPreColor = obj.style.backgroundColor;
		obj.style.backgroundColor = dragSrcColor;
		dragSrcCell = obj;

		dragMoveable = true;
		var pNode = getTargetParent(obj,"div");
		if(pNode){
			tableScrollLeft = pNode.scrollLeft;
		}
		pNode = getTargetParent(obj,"body");
		docScrollTop = parseInt(document.documentElement.scrollTop)+parseInt(pNode?pNode.scrollTop:0);
		oDiv.style.top = pos[0];
		oDiv.style.left = pos[1]-tableScrollLeft;
	}
}

function getTargetParent(_node,_nodeName){
	var _parentNode = _node.parentNode;
	var _r = null;;
	if(_parentNode){
		/*if(_parentNode.tagName.toLowerCase()=='body'){
			_r = null;
		}else */if(_parentNode.tagName.toLowerCase()==_nodeName){
			_r = _parentNode;
		} else {
			_r = getTargetParent(_parentNode,_nodeName);
		}
	}
	return _r;
}

// 隐藏拖动的div
function hideDragDiv() {
	if (dragMoveable) {
		dragMoveable = false;
		var oTable = document.getElementById(dragTableId);
		var thRow = oTable.rows[0];
		var pos = new Array();
		if (dragSrcCell != null) {
			var isSwap = false;
			var overCell;
			for (var i = 0; i < thRow.cells.length; i++) {
				overCell = thRow.cells[i];
				if (overCell.tagName.toLowerCase() == "th"&&overCell.getAttribute('moveTh')=='moveTh') {
					pos = getPos(overCell);
					//计算鼠标位置，是否在某个单元格的范围之内
					if (event.clientX > pos[1]-tableScrollLeft
							&& event.clientX < pos[1]-0 + overCell.offsetWidth-tableScrollLeft
							&& event.clientY > pos[0]-0 - docScrollTop
							&& event.clientY < pos[0]-0 + overCell.offsetHeight - docScrollTop) {
						if (overCell.tagName.toLowerCase() == "th") {
							isSwap = true;
							break;
						}
					}
				}
			}
			if(isSwap==true){
				ChangeColumn(oTable,dragSrcCell,overCell);
			}
			//清除原单元格和目标单元格的样式
			dragSrcCell.style.backgroundColor = dragSrcPreColor;
			overCell.style.backgroundColor = dragSrcPreColor;
			dragSrcCell.style.cursor = "";
			overCell.style.cursor = "";
			oTable.style.cursor = "";
		}
		//清除提示图层
		var dragDiv = document.getElementById(dragDivId);
		dragDiv.releaseCapture();
		dragDiv.style.display = "none";
		resetParam();
	}
}

//得到控件的绝对位置
function getPos(cell) {
	var pos = new Array();
	var t = cell.offsetTop;
	var l = cell.offsetLeft;
	while (cell = cell.offsetParent) {
		t += cell.offsetTop;
		l += cell.offsetLeft;
	}
	pos[0] = t;
	pos[1] = l;
	return pos;
}

// 是否是在th里面
function isInTh(_node){
	var _r = false ;
	if(_node){
		if(_node.nodeType==3){
			_node = _node.parentNode;
		}
		if(_node.tagName.toLowerCase()=='th'){
			_r = true;
		} else {
			_node = _node.parentNode;
		}
		if(_node.tagName.toLowerCase()=='th'){
			_r = true;
		} else {
			_node = _node.parentNode;
		}
		if(_node.tagName.toLowerCase()=='th'){
			_r = true;
		}
	}
}

function resetParam(){
	//是否能拖动
	dragMoveable = false;
	// 被拖动列
	dragSrcCell = null;
	dragSrcCellIndex = null;
	// 拖动源的原颜色
	dragSrcPreColor = null;;
	// 拖动的目标列
	dragOverCell = null;;
	dragOverCellIndex = null;

	// 拖动列头的坐标
	dragedTable_x0 = null;
	dragedTable_y0 = null;
	// 鼠标坐标
	dragedTable_x1 = null;
	dragedTable_y1 = null;
}

// ====================交换列end by chenyu=======================

// ====================表格页面排序start by chenyu================
var sortTableClass = 'sortTableClass';
// 图片路径
var imgDir = globalContextPath+'/style/easyuicss';
var unsortImg = 'unsort.png';
var sortImg = 'sort.png';
// 倒序箭头
var p_asc = "-16px -16px";
var p_desc = "-16px -0px";

// 表格排序功能入口
function sortTable(_obj) {
	var _th = _obj.parentNode;
	var col = _th.cellIndex;
	var _position = _obj.style.backgroundPosition;
	var sortImgRealPath = imgDir + '/' + sortImg;
	var tempPosition;
	if (_position != p_asc && _position != p_desc || _position == p_desc) {
		tempPosition = p_asc;
	} else if (_position == p_asc) {
		tempPosition = p_desc;
	}
	var oTable = document.getElementById(dragTableId);
	resetImg(oTable);
	if (tempPosition) {
		_obj.style.backgroundImage = 'url(' + sortImgRealPath + ')';
		_obj.style.backgroundPosition = tempPosition;
	}
	var oTable = document.getElementById(dragTableId);
	var oBody = oTable.tBodies[0];
	var dataRows = oBody.rows;
	// 列头
	var thRow = dataRows[0];
	var dataArr = new Array();
	for (var i = 0; i < dataRows.length; i++) {
		dataArr[i] = dataRows[i];
	}
	// 判断上次排序的列和这次是否为同一列
	if (oBody.currentCol == col) {
		// 数组反向排列
		dataArr.reverse();
	} else {
		var dataType = 'string';//getDataType(dataArr, col);
		var len = dataRows.length;
		var isDate = true;
		var isNumber = true;
		var isString = true;
//		var _int = /^(\-?)(\d+)$/;
		var _double = /^(-?\d+)(\.\d+)?$/;
		for(var i=1;i<len;i++){
			var _content = dataRows[i].cells[col].childNodes; 
			if(!_content||_content.length==0){
				continue;
			}
			var _data = dataRows[i].cells[col].firstChild.nodeValue;
			// 时间标识是true且当前数据不是时间时,将时间标识设置为false;
			if (isDate == true && !IsDate(_data) && !IsDateTime(_data)) {
				isDate = false;
			}
			// 数字标识是true且当前数据不是数字,则将数字标识设置为false;
			if (isNumber == true && !_double.test(_data)) {
				isNumber = false;
			}
		}
		if(isDate==true){
			dataType = 'date';
		}else if(isNumber==true){
			dataType = 'float';
		}
		dataArr.sort(customCompare(col, dataType));
	}
	// 创建一个临时空间，将所有的行都添加进去，排完序一次写回去
	var frag = document.createDocumentFragment();
	frag.appendChild(thRow);
	for (var i = 0; i < dataArr.length; i++) {
		if(dataArr[i]==thRow){
			continue;
		}
		frag.appendChild(dataArr[i]);
	}
	oBody.appendChild(frag);
	// 记录下当前排序的列
	oBody.currentCol = col;
	resetNo(oTable);
	if (typeof cscscs != undefined && cscscs instanceof Function) {
		cscscs();
	}
}

// 重置排序图标
function resetImg(oTable){
	if(oTable){
		var spans = oTable.getElementsByTagName('span');
		if(spans){
			var unsortImgRealPath = imgDir + '/' + unsortImg ;
			var spanLen = spans.length;
			for(var i=0;i<spanLen;i++){
				var _span = spans[i];
				if(_span&&_span.className==sortTableClass){
					_span.style.backgroundImage = 'url('+unsortImgRealPath+')';
					_span.style.backgroundPosition = '-0px -0px';
				}
			}
		}
	}
}

// 重置序号
function resetNo(oTable){
	if(oTable){
		var rows = oTable.rows;
		if(rows){
			var rowNum = rows.length;
			var thRow = rows[0];
			if(thRow){
				if(thRow.cells[0].firstChild.nodeValue!='序号'){
					return;
				}
			}
			for(var i=1;i<rowNum;i++){
				rows[i].cells[0].firstChild.nodeValue = (i-0);
			}
		}
	}
}

function customCompare(col, dataType) {
	return function compareTRs(tr1, tr2) {
		var value1, value2;
		// 列头不参与排序
		if(tr1.rowIndex==0||tr2.rowIndex==0){
			return 0;
		}
		// 根据内容是否为空排序
		var cell1 = tr1.cells[col];
		var cell2 = tr2.cells[col];
		var _content1 = cell1.childNodes;
		var _content2 = cell2.childNodes;
		// 如果都为空,则返回0
		if((!_content1&&!_content2)||(_content1.length==0&&_content2.length==0)){
			return 0;
		}
		// tr1为空且tr2不为空,返回-1
		if((!_content1||_content1.length==0)&&(_content2||_content2.length>0)){
			return -1;
		}
		// tr2为空且tr1不为空,返回1
		if((_content1||_content1.length>0)&&(!_content2||_content2.length==0)){
			return 1;
		}
		
		// 判断是不是有customvalue这个属性,用于图片之类的排序,图片用图片备注或者名排序
		if (tr1.cells[col].getAttribute("customvalue")) {
			value1 = convert(tr1.cells[col].getAttribute("customvalue"),
					dataType);
			value2 = convert(tr2.cells[col].getAttribute("customvalue"),
					dataType);
		} else {
			var nodeValue1 = getRealValue(cell1);
			var nodeValue2 = getRealValue(cell2);
			
			value1 = convert(nodeValue1, dataType);
			value2 = convert(nodeValue2, dataType);
		}
		var _r = 0;
		// 都是空值的话,返回0
		if ((!value1 || value1.length == 0) 
				&& (!value2 || value2.length == 0)) {
			_r = 0;
		}
		// tr1是空值且tr2不是空值,返回-1
		else if ((!value1 || value1.length == 0)
				&& !(!value2 || value2.length == 0)) {
			_r = -1;
		}
		// tr1不是空值tr1是空值,返回1
		else if (!(!value1 || value1.length == 0)
				&& (!value2 || value2.length == 0)) {
			_r = 1;
		} else if (value1 < value2) {
			_r = -1;
		} else if (value1 > value2) {
			_r = 1;
		} else {
			_r = 0;
		}
		return _r;
	};
}
function convert(dataValue, dataType) {
	if(!dataValue){
		return null;
	}
	switch (dataType) {
	case "int":
		return parseInt(dataValue);
	case "float":
		return parseFloat(dataValue);
	case "date":
		return strToDate(dataValue);
	default:
		return dataValue.toString();
	}
}

// 获取单元格的值
function getCellValue(_node){
	var _r = null;
	if(_node){
		// 如果是文本节点
		if (_node.nodeType == 3) {
			_r = _node.nodeValue;
		}
		// 如果是输入框
		else if (_node.nodeType == 1 && _node.tagName.toLowerCase() == 'input'
				&& _node.getAttribute('type') == 'text') {
			_r = _node.value;
		}
		// 如果是元素节点,且还有子节点
		else if (_node.nodeType == 1 && _node.childNodes.length > 0) {
			for(var i = 0;i<_node.childNodes.length;i++){
				var tValue = getCellValue(_node.childNodes[i]);
				if(tValue&&tValue.length>0){
					_r = tValue;
					break;
				}
			}
		} else {
			_r = '';
		}
	}
	return _r;
}

// 获取单元格真正的值,会跳过不是文本节点的内容
// 必须保证值在firstNode或者递归的firstNode中
function getRealValue(_node){
	var _r = null;
	if(_node){
		// 如果是文本节点
		if (_node.nodeType == 3) {
			_r = _node.nodeValue;
		}
		// 如果是输入框
		else if (_node.nodeType == 1 && _node.tagName.toLowerCase() == 'input'
				&& _node.getAttribute('type') == 'text') {
			_r = _node.value;
		}
		// 如果是元素节点,且还有子节点
		else if (_node.nodeType == 1 && _node.childNodes.length > 0) {
			_r = getRealValue(_node.firstChild);
		} else {
			_r = '';
		}
	}
	return _r;
}

// ====================表格页面排序end by chenyu===================

  /* 增加String的format方法 */
	Object.extend(String, {
		  format: function(str) {
			try {
			  return str;
			} catch (e) {
			  throw e;
			}
		  }
		 });
	
	// 删除grid
	function removeGird(id){
		$(id).innerHTML = '';
	}
    
    /**
     * created by andy.ten@tom.com
     * tabArr:生成列表数组对象
     * dataArr:dataIndex数组对象
     * funcArr：render数组对象
     * 生成表头功能
     */
    function createTableHead(tabArr,dataArr,funcArr,renderParArr,CurGrid)
    {
    	dragDivInit();
    	tabArr.push("<table id='myTable' class='table_list' style='border-bottom:1px solid #DAE0EE;' ondrag='return false;'>");
    	//生成合并表头
		var rowspan;
		rowspan = this.columns[0].rowspan;
		if(this.columns && this.columns[0])
		{
		    rowspan = this.columns[0].rowspan ? this.columns[0].rowspan : 1;
		}
		else
		{
			return ;
		}
	    for(var n=1; n<=rowspan; n++)
	    {
		    tabArr.push("<tr class='table_list_th'>");
		    var columnColspan = 0;
		    var count = 0;
	    	for(var i=0; i< this.columns.length; i++)//列名
			{
				var columnObj = this.columns[i];
				if(n==1)
				{
					dataArr.push(columnObj.dataIndex);//记录dataIndex				
					funcArr.push(columnObj.renderer);  //记录renderer
					renderParArr.push((columnObj.renderParValue));
				}
				var columnRowspan = columnObj.rowspan;
				if(n==1 && columnObj.colspan) {
					previousColspan = columnColspan;
					columnColspan = columnObj.colspan;
				}
	    		if(columnObj.orderCol)
	    		{
					if($("orderCol").value == columnObj.orderCol||$("orderCol").value.split("-")[0] == columnObj.orderCol)
					{
						if($("order").value == '-1')
						{
							colMask = "descMask";						
						}else if($("order").value == '1')
						{
							colMask = "ascMask";
						}
					}else
					{
						colMask = "sortMask";
					}
				}else
				{
					colMask = "noSort";
				}
				if(!columnObj.width)
				{
					if(n==1)
					{
						if(columnRowspan)
						{
						   tabArr.push("<th class='"+ colMask + "' rowspan='"+columnRowspan+"' >" + columnObj.header + "</th>");	
						}
					    else if(columnColspan)
					    {
					    	if(/*count == columnColspan || */previousColspan == count) {
					    		previousColspan = columnColspan;
					    		count = 0;
					    	}
					    	if(columnColspan && columnColspan > 0 && count < columnColspan)
					    	{
					    		if(count == 0)
					    		{
					        		tabArr.push("<th class='"+ colMask + "' colspan='"+columnColspan+"'>" + columnObj.colspanName + "</th>");
					        		count ++ ;
					    		}else
					    		{
					    			count ++ ;
					    			continue;
					    		}	
					    	}
					    }else 
					    {
					    	var tableTh = "<th class='"+ colMask + "' onselectstart='return false;' style='word-wrap: break-word;word-break: break-all;'";
					    	// =================增加表格排序拖动的功能start by chenyu================
					    	var _operate = isWithoutHeader(columnObj.header,i)==false?true:false;
					    	if(_operate){
						    //if(_operate&&CurGrid.swapColumnFlag==true){
					    		tableTh += " onmousedown='movespan(this);' moveTh='moveTh'";
					    	}
					    	tableTh += ">";
			    			if(_operate){
				    			// 调整列宽标记
//			    				if(CurGrid.resizeColumnWidthFlag==true){
			    					tableTh += createResizeNode();
//			    				}
//			    				if(CurGrid.tableSortflag==true){
			    					tableTh += createSortNode();
//			    				}
				    			
			    			}
					    	// =================增加表格排序拖动的功能end by chenyu================
			    			tableTh += columnObj.header ;
					    	// =================增加保存用户选中记录的功能start by chenyu===========
			    			// 在全选框后面增加一个隐藏域,存用户选中的复选框value
			    			if(hasSelectAll(columnObj.header)){
			    				tableTh += createHideCheckedNode();
			    			}
					    	// =================增加保存用户选中记录的功能end by chenyu=============
			    			tableTh += "</th>";
					    	tabArr.push(tableTh);
					    }
					}else if(!columnRowspan)
			        {
			      		tabArr.push("<th class='"+ colMask + "' >" + columnObj.header + "</th>");
			        }
					    
				}else
				{	
					if(n==1)
					{
						if(columnRowspan)
						{
						   tabArr.push("<th class='"+ colMask +"'"+" width=" + columnObj.width +" rowspan='"+columnRowspan+"' >" + columnObj.header + "</th>");	
						}
					    else if(columnColspan)
					    {
					    	if(/*count == columnColspan*/previousColspan == count) {
					    		previousColspan = columnColspan;
					    		count = 0;
					    	}
					    	if(columnColspan && columnColspan > 0 && count < columnColspan)
					    	{
					    		if(count == 0)
					    		{
					        		tabArr.push("<th class='"+ colMask + "'" + " width=" + columnObj.width  + "'  colspan='"+columnColspan+"'>" + columnObj.colspanName + "</th>");
					        		count ++ ;
					    		}else
					    		{
					    			count ++ ;
					    			continue;
					    		}	
					    	}
					    }else 
					    {
					    	var tableTh = "<th class='"+ colMask + "' style='word-wrap: break-word;word-break: break-all;'";
					    	// =================增加表格排序拖动的功能start by chenyu================
					    	var _operate = isWithoutHeader(columnObj.header,i)==false?true:false;
					    	if(_operate){
						    //if(_operate&&CurGrid.swapColumnFlag==true){
					    		tableTh += " onmousedown='movespan(this);' moveTh='moveTh'";
					    	}
					    	tableTh += "1>";
			    			if(_operate){
				    			// 调整列宽标记
//			    				if(CurGrid.resizeColumnWidthFlag==true){
			    					tableTh += createResizeNode();
//			    				}
//			    				if(CurGrid.tableSortflag==true){
			    					tableTh += createSortNode();
//			    				}
				    			
			    			}
					    	// =================增加表格排序拖动的功能end by chenyu================
			    			tableTh += columnObj.header ;
						    // =================增加保存用户选中记录的功能start by chenyu===========
				    		// 在全选框后面增加一个隐藏域,存用户选中的复选框value
				    		if(hasSelectAll(columnObj.header)){
				    			tableTh += createHideCheckedNode();
				    		}
						    // =================增加保存用户选中记录的功能end by chenyu=============
				    		tableTh += "</th>";
					    	tabArr.push(tableTh);
					    }
					}else if(!columnRowspan)
			        {
			      		tabArr.push("<th class='"+ colMask + "' " + "width=" + columnObj.width +" >" + columnObj.header + "</th>");
			        }
				}
			}
			tabArr.push("</tr>");	
	    }
	    //alert("debug:"+dataArr);
	    //alert(tabArr);	
    }
    //end 
    //added by andy.ten@tom.com
    
    function customTableFunc(tabObj)
    {
    }
    //end
	function createGrid(title, columns, cnt, ps){
		createGrid.backColor = "#FDFDFD";	
		createGrid.hoverColor = "#BDEDCD";
		createGrid.clickColor = "#BDEDCD";

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
		this.resizeColumnWidthFlag = ps.resizeColumnWidthFlag;
		this.tableSortflag = ps.tableSortflag;
		this.swapColumnFlag = ps.swapColumnFlag;

		var CurGrid = this;
 
		this.load = function(){//grid重画模块
//		    if(g_webAppName.length !=9)
//    		{
//    			alert("未经授权，请与andy.ten@tom.com联系");
//    			return false;
//    		}
			if($('myTable') != null){
				removeGird(this.container);
			}
			var tbStr = [], dataIndexArr = [], rendererArr = [],renderParValue = [], cellCnt=[],index,noWrap,colMask;
			/**
			 * modified by andy.ten@tom.com
			 */		
			createTableHead(tbStr,dataIndexArr,rendererArr,renderParValue,CurGrid);
		    //end
			
			//added by andy.ten@tom.com 如有计算功能，获取计算配置信息
			var bindTableList,subTotalColumns,subTotalScolumns,totalColumns,totalSumMap,subTotalSumMap;
			
			try
			{
				if(calculateConfig)
				{
				    if(calculateConfig.bindTableList)
						bindTableList = calculateConfig.bindTableList;
					var s;
					if(calculateConfig.subTotalColumns)
						s = calculateConfig.subTotalColumns;
					if(s && s.indexOf("|") >0)
					{
						subTotalColumns = s.split("|")[0];
						subTotalScolumns = s.split("|")[1];
					}
					if(calculateConfig.totalColumns)
						totalColumns = calculateConfig.totalColumns;
					if(totalColumns)
						totalSumMap = new HashMap();						
				}
			}catch(e)
			{}	
			//end
			
			var rowColor = -1;
			for(var i=0; i< this.jsonData.length;i++){//
				
				var rowsum = 0;
				var calculateFlag = false;
				
				//modified by andy.ten@tom.com
				switch(rowColor)
				{
					case -1:
					        if((i & 1)==1)
							{
								tbStr.push("<tr class='table_list_row1' style='background-color: #F7F7F7'>");
								rowColor = 2;
							}else
							{
								tbStr.push("<tr class='table_list_row1'>");
								rowColor = 1;
							}
					  break;
					case  1:
					        tbStr.push("<tr class='table_list_row1' style='background-color: #F7F7F7'>");
							rowColor = 2;
					  break;
					case  2:
					        tbStr.push("<tr class='table_list_row1'>");
					        rowColor = 1;
					  break;
				}
				
				//end
				var subTotalIndex = 0;
				for(var j=0;j<dataIndexArr.length;j++){	
					
					//added by andy.ten@tom.com
					if(!subTotalSumMap)
					    subTotalSumMap = new HashMap();
					    
					cellCnt = this.jsonData[i][dataIndexArr[j]];//根据dataIndex显示后台数据
					//alert("测试222222："+this.jsonData[i][dataIndexArr[j]]);
					if(cellCnt == null || cellCnt == undefined){
						cellCnt ='';
					}
					//alert(this.jsonData[i][dataIndexArr[j]] == undefined);
					
					if(typeof(rendererArr[j])=='function'){//列名有renderer属性 
						var __data__ = {};
						__data__.data = this.jsonData[i];
						if(this.columns[j].renderParValue!=''&&this.columns[j].renderParValue!=null){
							cellCnt = this.columns[j].renderer(cellCnt,{},__data__,__data__.data[this.columns[j].renderParValue]);//显示renderer函数，传值	
						}else{
							cellCnt = this.columns[j].renderer(cellCnt,{},__data__);//显示renderer函数，传值		 
						}
						
					}
					
					if(this.columns[j].style == undefined){
						styleV = '';
					}else{
						styleV = this.columns[j].style;
					}
					
					if(this.columns[j].isColor == undefined){
			  			tbStr.push("<td style='"+ styleV +"'>" + cellCnt + "</td>");
					}else{
					  
					  if(this.columns[j].isColor == 'true'){
					  	tbStr.push("<td  style='padding:2px;background:"+cellCnt+"'>" + '&nbsp;' + "</td>");}
					  else{
					  	tbStr.push("<td  bgcolor='"+cellCnt+"'>&nbsp;" + cellCnt + "&nbsp;</td>");
					  	}
					}
					
					//added by andy.ten@tom.com
					var totalIndex = 0;
					if(totalColumns && totalColumns == dataIndexArr[j])
					{
						totalIndex = j;
					}
					if(totalColumns && totalIndex < j)
					{
						if(totalSumMap.get(dataIndexArr[j]))
						{
							 var num = this.jsonData[i][dataIndexArr[j]];
							 if(!num)  num = 0.00;
						     totalSumMap.put(dataIndexArr[j],parseFloat((totalSumMap.get(dataIndexArr[j]) + parseFloat(num)),10));
						}     
						else
						{
							 var num = this.jsonData[i][dataIndexArr[j]];
							 if(!num)  num = 0.00;
						     totalSumMap.put(dataIndexArr[j],parseFloat(num,10));
						}    
					}
					var sIndex = 0;
					if(subTotalColumns && subTotalColumns == dataIndexArr[j])
					{
						sIndex = j;
						subTotalIndex = j;
					}
					if(subTotalColumns && sIndex < j)
					{
						if(subTotalSumMap.get(dataIndexArr[j]))
						{
							 var num = this.jsonData[i][dataIndexArr[j]];
							 if(!num)  num = 0.00;
						     subTotalSumMap.put(dataIndexArr[j],parseFloat((subTotalSumMap.get(dataIndexArr[j]) + parseFloat(num)),10));
						}     
						else
						{
							 var num = this.jsonData[i][dataIndexArr[j]];
							 if(!num)  num = 0.00;
						     subTotalSumMap.put(dataIndexArr[j],parseFloat(num,10));
						}    
					}
					//end	     
					
				}
				tbStr.push("</tr>");
				//added by andy.ten@tom.com
				if(subTotalColumns && i < this.jsonData.length)
				{
					var curRows,nextRows,curColumnVal,nextColumnVal;
					if(i < this.jsonData.length-1)
					{
					   curRows = this.jsonData[i];
					   nextRows = this.jsonData[i+1];
					   curColumnVal = curRows[subTotalColumns];
					   nextColumnVal = nextRows[subTotalColumns];
					   if(curColumnVal == nextColumnVal)
				        	calculdateFlag = false;
				       else
				       {
				            calculdateFlag = true;
				       }     
					}else
					{
					   calculdateFlag = true;
					}
					if(calculdateFlag == true)
				    {
				        //calculdateFlag = true;
				        switch(rowColor)
						{
							case -1:
							        if((i & 1)==1)
									{
										tbStr.push("<tr class='table_list_row2'>");
										rowColor = 2;
									}else
									{
										tbStr.push("<tr class='table_list_row1'>");
										rowColor = 1;
									}
							  break;
							case  1:
							        tbStr.push("<tr class='table_list_row2'>");
									rowColor = 2;
							  break;
							case  2:
							        tbStr.push("<tr class='table_list_row1'>");
							        rowColor = 1;
							  break;
						}
						
						var colspan = -1;
						for(var n=0;n<dataIndexArr.length;n++)
						{
							if(subTotalScolumns == dataIndexArr[n])
							{
								colspan = n ;
							}
							if(colspan > 0)
							{
								if(colspan == n)
								{
									var cellCnt = "";
									for(var len=0;len<this.columns.length;len++)
									{
										if(this.columns[len].dataIndex == subTotalColumns)
										{
											cellCnt = this.jsonData[i][subTotalColumns];
											if(this.columns[len].renderer)
							   				{
												var __data__ = {};
												cellCnt = this.columns[len].renderer(cellCnt,{},__data__);//显示renderer函数
							   				}
										}
									}
									
							   		tbStr.push("<td><strong>"+cellCnt+"&nbsp;小计 ：</strong></td>");
								}	
							   	else
							   	{
					                var cellCnt = subTotalSumMap.get(dataIndexArr[n]);
							   		if(typeof(rendererArr[n])=='function')
							   		{
										var __data__ = {};
										cellCnt = this.columns[n].renderer(cellCnt,{},__data__);//显示renderer函数
										
										if(isNaN((cellCnt+"").replace(/\,/g,"")))//将结果替换掉逗号,以供金额显示
								   		{
								   			cellCnt = "";
								   		}
								   	    tbStr.push("<td style='text-align:right'>"+cellCnt+"</td>");
							   		}
							   		else
							   		{
							   			tbStr.push("<td style='text-align:right'>&nbsp;</td>");
							   		}
							   	 }    	
							}else
							{
								tbStr.push("<td>&nbsp;</td>");
							}
						}
						tbStr.push("</tr>");
						subTotalSumMap = null;
				    }    
					
				}
				
			}
			//end							
			//added by andy.ten@tom.com
			if(totalColumns)
			{
				switch(rowColor)
				{
					case -1:
					        if((i & 1)==1)
							{
								tbStr.push("<tr class='table_list_row2'>");
								rowColor = 2;
							}else
							{
								tbStr.push("<tr class='table_list_row1'>");
								rowColor = 1;
							}
					  break;
					case  1:
					        tbStr.push("<tr class='table_list_row2'>");
							rowColor = 2;
					  break;
					case  2:
					        tbStr.push("<tr class='table_list_row1'>");
					        rowColor = 1;
					  break;
				}
				var colspan = -1;
				for(var n=0;n<dataIndexArr.length;n++)
				{
					if(totalColumns == dataIndexArr[n])
					{
						colspan = n ;
					}
					if(colspan > 0)
					{
						if(colspan == n)
					   		tbStr.push("<td><strong>本页合计 ：</strong></td>");
					   	else
					   	{
					   		
			                var cellCnt = totalSumMap.get(dataIndexArr[n]);
			                if(isNaN(cellCnt))
					   		{
					   			 cellCnt = "";
					   		}
					   		if(typeof(rendererArr[n])=='function')
					   		{
								var __data__ = {};
								cellCnt = this.columns[n].renderer(cellCnt,{},__data__);//显示renderer函数
					   		}
					   			 	
					   	    tbStr.push("<td><strong>"+cellCnt+"</strong></td>");
					   	 }    	
					}else
					{
						tbStr.push("<td></td>");
					}
				}
				tbStr.push("</tr>");
			}
			//end
			tbStr.push("</table>");
			this.container.innerHTML = tbStr.join("");
			this.table = this.container.firstChild;

			if(this.title != null){//表格标题	
				var x = $('myTable').createCaption();
				x.innerHTML = "<span class='navi'>&nbsp;</span>"+this.title;
			}

			/** 设置单元格  **/
			for(var r=1; r<this.table.rows.length;r++){
	            //added by andy.ten@tom.com
	            var firstCell = this.table.rows[r].cells[0];
	            if(firstCell && firstCell.firstChild && firstCell.firstChild.type == "checkbox" || firstCell && firstCell.firstChild && firstCell.firstChild.type == "radio")
	            	this.table.rows[r].ondblclick = new Function("doRowClick(this)");
	            //end
				if(dataIndexArr[0] == undefined || rendererArr[0] == "function getIndex(){}"){//序号判断
				
					if(this.curPage == 1){//计算序号
						index = r;
					}else{
						index = parseInt(this.curPage-1)*this.pageSize + r;
					}			
					this.table.rows[r].cells[0].innerHTML = index; 
					this.table.rows[r].cells[0].style.textAlign = 'center';//序号单元格居中			
				}
				var backgroundColor="";
				var oldColor="";
				// mouseover时原背景颜色
				var preOverColor = "preOverColor";
				// row是否被点击过,如果点击过,mouseout时,用另一种方式变颜色
				var clickFlag = "clickFlag";
				
				this.table.rows[r].onmouseover = function(){
					var c=this.style.backgroundColor;
					this.setAttribute(preOverColor,c);
					this.removeAttribute(clickFlag);
					this.closure=function(){
						return c;
					}
					if(CurGrid.curRow!=this){
						this.setAttribute("oldColor",this.style.backgroundColor);
					}
					this.style.backgroundColor ="#BDEDCD"; 
					
				}
				this.table.rows[r].onmouseout = function(){
					// 是否有点击动作
					if(this.getAttribute(clickFlag)){
						if(this.getAttribute(preOverColor)){
							if(this.getAttribute(preOverColor).toLowerCase()==createGrid.clickColor.toLowerCase()){
								this.style.backgroundColor = this.getAttribute("oldColor");
							} else {
								this.style.backgroundColor = createGrid.clickColor
							}
						}
					}else {
						if(CurGrid.curRow!=this){ this.style.backgroundColor = this.getAttribute("oldColor")} 
						else this.style.backgroundColor = createGrid.clickColor;
					}
				}
	
				for(var c=0;c<this.table.rows[r].cells.length;c++)
				{
				    // added by andy.ten@tom.com
				    var cell = this.table.rows[r].cells[c];
				    var cellHTML = cell.innerHTML;
				    if(cell && cell.innerText && typeof(cellHTML) == "string" && cellHTML.indexOf("href") == -1)
				    {
				        var cellText = cell.innerText;
				        if(cellText.length)
				        {
				        	if(cell.showsize && cell.showsize > 0)
				        	{
				        		this.table.rows[r].cells[c].innerText = cellText.substr(0,showsize) + "...";
				        		this.table.rows[r].cells[c].title = cellText;
				        	}else if(cellText.length > 40)
				        	{
				        		this.table.rows[r].cells[c].innerText = cellText.substr(0,40) + "...";
				        		this.table.rows[r].cells[c].title = cellText;
				        	}
				        }	
				    }
				    // end	
					this.table.rows[r].cells[c].onclick = function()
					{

						if(CurGrid.curRow) {
							CurGrid.curRow.style.backgroundColor = CurGrid.curRow.getAttribute("oldColor");
						}
						
						CurGrid.curRow = this.parentNode;
						CurGrid.curRow.style.backgroundColor = createGrid.clickColor;
						CurGrid.curRow.setAttribute(clickFlag,"true");
						
					}
	
				}
			}

			for(var g=0; g<this.table.rows[0].cells.length;g++){
				this.table.rows[0].cells[0].style.textAlign = 'center';//序号列居中
				if(this.columns[g].orderCol != undefined){
					this.table.rows[0].cells[g].onclick = function(){

						var _order = 1;
						if(!$("queryBtn").disabled){//亮
							//if(CurGrid.table.rows[0].cells[this.cellIndex].innerHTML.lastIndexOf('▲')!= -1){
							if(CurGrid.table.rows[0].cells[this.cellIndex].className == "ascMask"){
								_order = '-1';
							}								
						}else{
							if($("orderCol").value != this.cellIndex){return false;}
							if(CurGrid.table.rows[0].cells[this.cellIndex].className == "ascMask"){
								_order = '1';
							}
							if(CurGrid.table.rows[0].cells[this.cellIndex].className == "descMask"){
								_order = '-1';
							}
						}
//						CurGrid.sort(this.cellIndex, CurGrid.columns[this.cellIndex].orderCol,_order,CurGrid.columns[this.cellIndex].orderType);	

					}
				}
			}
			//added by andy.ten@tom.com
			customTableFunc(this.table);
			//end
		}
	
		this.sort = function(n, orderCol,order,orderType){  //排序 n-列 type-升降序
		
			if(typeof(this.remoteSort) == 'undefined' || this.remoteSort == false){//当前页排序			
				this.jsonData = this.jsonData.sort(function(x,y){
					if (x[n]>y[n]){return type;}else if(x[n]<y[n]){return -type;}else{return 0;}});
			}else{//远程排序
				if($('myTable') != null){
					removeGird(this.container);
				}
				myRemoteSort(orderCol,order,orderType);
			}
																					
			this.load();
		
		}
		
	}
	
	//远程排序接口
	function myRemoteSort(orderCol,order,orderType){
		//排序类型 0:不排 -1:降序 1:升序
		if(orderType!=undefined&&orderType!=""){
			$("orderCol").value = orderCol+"-"+orderType;
		}else{
			$("orderCol").value = orderCol;
		}
		$("order").value = order;

		if(!$("queryBtn").disabled){//亮
			__extQuery__(1);
		}
		//alert("当前列="+col+" | "+"排序类型="+order +" | "+ "此为排序接口，远程请求在此写！");
	}

	function getIndex(){}

	function hiddenDocObject(flag){
	try{

	    if(HIDDEN_ARRAY_IDS){
	       for(var i=0;i<HIDDEN_ARRAY_IDS.length;i++){
	          if($(HIDDEN_ARRAY_IDS[i]) && flag==1)
	          	$(HIDDEN_ARRAY_IDS[i]).hide();
	          else if($(HIDDEN_ARRAY_IDS[i]) && flag==2)
	             $(HIDDEN_ARRAY_IDS[i]).show();
	       }
	    }
	    }catch(ee){
	    }
	}
	//Ajax返回调用函数 设置字段、列名属性参数
	function callBack(json){
		var ps;
		var checkVals;
		var checkParams;
		var customPageSizeFlag;
		var tableSortflag;
		var resizeColumnWidthFlag;
		var swapColumnFlag;
		//设置对应数据
		if(Object.keys(json).length>0){
			keys = Object.keys(json);
			for(var i=0;i<keys.length;i++){
			   if(keys[i] =="ps"){
				   ps = json[keys[i]];
//				   break;
			   }
			   // ==================复选框相关的参数start by chenyu=================
			   if(keys[i] == "checkParams"){
				   checkParams = json[keys[i]];
			   }
			   if(keys[i] == "checkedValues"){
				   checkVals = json[keys[i]];
			   }
			   // ==================复选框相关的参数end by chenyu===================
			   // ==================各个开关start by chenyu===================
			   // 自定义每页条数
			   if(keys[i] == "customPageSizeFlag"){
				   customPageSizeFlag = json[keys[i]];
			   }
			   // 列头排序
			   if(keys[i] == "tableSortflag"){
				   tableSortflag = json[keys[i]];
			   }
			   // 列宽调整
			   if(keys[i] == "resizeColumnWidthFlag"){
				   resizeColumnWidthFlag = json[keys[i]];
			   }
			   // 列交换
			   if(keys[i] == "swapColumnFlag"){
				   swapColumnFlag = json[keys[i]];
			   }
			   // ==================各个开关start by chenyu===================
			}
			
		//	ps = json[Object.keys(json)[0]]; 
		}
		//生成数据集
		if(ps.records != null){
			// ==================各个开关start by chenyu===================
			// 每页条数
			if(customPageSizeFlag){
				ps.customPageSizeFlag = customPageSizeFlag;
			}
			// 列头排序
			if(tableSortflag){
				ps.tableSortflag = tableSortflag;
			}
			// 调整列宽
			if(resizeColumnWidthFlag){
				ps.resizeColumnWidthFlag = resizeColumnWidthFlag;
			}
			// 交换列
			if(swapColumnFlag){
				ps.swapColumnFlag = swapColumnFlag;
			}
			// 冻结列数
			if (typeof Options != 'undefined' && Options.cells) {
				withoutCells = Options.cells;
			}
			// ==================各个开关end by chenyu=====================
			$("_page").hide();
			$('myGrid').show();
			new createGrid(title,columns, $("myGrid"),ps).load();
			//分页
			myPage = new showPages("myPage",ps,url);
			myPage.printHtml();
			hiddenDocObject(2);
			// ==================复选框相关的值初始化start by chenyu=================
			if(checkParams&&Object.keys(json).length>0){
				keys = Object.keys(json);
				for(var j=0;j<checkParams.length;j++){
					for(var i=0;i<keys.length;i++){
						if(keys[i] == checkParams[j]){
							$(checkParams[j]).value=json[keys[i]];
							break;
						}
					}
				}
			}
			// ==================复选框相关的值初始化end by chenyu===================
			initCheckboxChecked(checkVals,columns);
			setGridCss();
		}else{
			$("_page").show();
			$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
			$("myPage").innerHTML = "";
			removeGird('myGrid');
			$('myGrid').hide();
			hiddenDocObject(1);
		}
		//useableBtn($("queryBtn"));
	}
	
	function setGridCss(){
		setTableLayoutToFixed();
		// 动态设置tablediv的高度
		setGridHeight();
		
		//2015-05-08 ranke 冻结列
		if (typeof Options != 'undefined' && Options.cells) {//冻结列使用
			document.getElementById('myGrid').style.overflowX = "hidden";
			frozenColumn();
		}
	}
	
/*============================================分页=================================================*/

	function showPages(name,ps,url) { //初始化属性
		this.name = name; //对象名称
		this.url = url; //action
		this.page = ps.curPage; //当前页数
		this.pageCount = ps.totalPages; //总页数
		this.totalRecords = ps.totalRecords; //总记录数
		this.pageSize = ps.pageSize;  // 每页条数
		this.customPageSizeFlag = ps.customPageSizeFlag; // 自定义每页条数开关
	}
	
	
	
	showPages.prototype.getPage = function(){ //丛url获得当前页数,如果变量重复只获取最后一个
		var args = location.search;
		var reg = new RegExp('[\?&]?' + this.argName + '=([^&]*)[&$]?', 'gi');
		var chk = args.match(reg);
		this.page = RegExp.$1;
	}
	showPages.prototype.checkPages = function(){ //进行当前页数和总页数的验证
		if (isNaN(parseInt(this.page))) this.page = 1;
		if (isNaN(parseInt(this.pageCount))) this.pageCount = 1;
		if (this.page < 1) this.page = 1;
		if (this.pageCount < 1) this.pageCount = 1;
		if (this.page > this.pageCount) this.page = this.pageCount;
		this.page = parseInt(this.page);
		this.pageCount = parseInt(this.pageCount);
	}
	showPages.prototype.createHtml = function(){ //生成html代码
		var strHtml = '', prevPage = this.page - 1, nextPage = this.page + 1;
		//如果定义了GOLB_HIDE_HREF这个变量，则在查询的结果集只有一页的时候，不显示右下脚的分页显示信息
		if(this.pageCount>1){
			// =============表格缩放功能start by chenyu================================
			strHtml += createChangeHeightNode();
			// =============表格缩放功能end by chenyu==================================
			// =============自定义每页条数功能start by chenyu================================
			if(this.customPageSizeFlag==true){
				strHtml += createCustomPageSizeNode();
			}
			// =============自定义每页条数功能end by chenyu==================================
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
			strHtml += '<input type="text" id="pageInput' + '" value="' + this.page + '" class="mini_txt" title="Input page" onkeydown="return ' + this.name + '.formatInputPage(event);" onfocus="this.select()" style="margin-left:-10px;">&nbsp;';
			strHtml += '<input type="button" name="go" value="Go" class="mini_btn" onclick="' + this.name + '.toPage(document.getElementById(\'pageInput' + '\').value);">';
			}
			strHtml += '</div>';
		}
		return strHtml;
	}

	showPages.prototype.createUrl = function (page) { //生成页面跳转url
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

	showPages.prototype.toPage = function(page){ //页面跳转
	  /*var turnTo = 1;
	  if (typeof(page) == 'object') {
		turnTo = page.options[page.selectedIndex].value;
	  } else {
		turnTo = page;
	  }
	  self.location.href = this.createUrl(turnTo);*/
	  //removeGird('myGrid');
	  if(page>=1 && page<=this.pageCount)
	  	__extQuery__(page);
	  //makeFormCall(this.url+"&curPage="+page,callBack,'fm');
	}
	
	showPages.prototype.printHtml = function(){ //显示html代码
  		//this.getPage();	
		//this.checkPages();
		//var pageBox = this.name;
		$(this.name).innerHTML = this.createHtml();
		var _pageSize = document.getElementById('mainPageSize');
		if(_pageSize){
			_pageSize.value=this.pageSize;
		}
		//callBack();
	}

	showPages.prototype.formatInputPage = function(evt){ //限定输入页数格式
		var evt=evt?evt:(window.event?window.event:null);
		if(evt.keyCode == 13 && $("pageInput").value>=1 && $("pageInput").value<=this.pageCount){
			__extQuery__($F("pageInput"));	
		}
		/*var ie = navigator.appName=="Microsoft Internet Explorer"?true:false;
		if(!ie) var key = e.which;
		else var key = event.keyCode;
		if (key == 8 || key == 46 || (key >= 48 && key <= 57)) return true;
		return false;*/
	 }

	 
	 /*============================================查询效果 部分===========================================*/
		function disableBtn(obj){
			obj.disabled = true;
			obj.style.border = '1px solid #999';
			obj.style.background = '#EEE';
			obj.style.color = '#999';
			showMask();
		}
		function disableBtn2(obj){
			obj.disabled = true;
			obj.style.border = '1px solid #999';
			obj.style.background = '#EEE';
			obj.style.color = '#999';
			//showMask();
		}
		function useableBtn(obj){
			obj.disabled = false;
			obj.style.border = '1px solid #5E7692';
			obj.style.background = '#EEF0FC';
			obj.style.color = '#1E3988';
			removeMask();
		}
		
		function showMask(){
			if($('loader')){
				var screenW = document.viewport.getWidth()/2;	
				$('loader').style.left = (screenW-20) + 'px';
				$('loader').innerHTML = ' 正在载入中... ';
				$('loader').show();
			}
		}
		
		function removeMask(){
			if($('loader'))
				$('loader').hide();
		}
		
		/**function showMask(){
			var divObj = document.createElement("div");
			divObj.innerHTML = '<div>正在载入中...</div>';
			var screenW = document.viewport.getWidth()/2;	
			divObj.style.left = screenW + 'px';
			divObj.style.zIndex = 200;
			divObj.style.position = 'absolute';
			divObj.style.background = '#FFCC00';
			divObj.style.padding = '1px';
			divObj.style.top = '4px';
			divObj.id = 'loader';	
			document.body.appendChild(divObj);	
		}
		
		function removeMask(){
			$('loader').remove();
		}*/
        //add by zhaolunda 2010-06-13 重新生成列
        function reCreateGrid(title, columns, cnt, ps){
        	createGrid.backColor = "#FDFDFD";	
        	createGrid.hoverColor = "#EEEEEE";
        	createGrid.clickColor = "#EEEEEE";

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

        		if($('myTable') != null){
        			removeGird(this.container);
        		}
        		var tbStr = [], dataIndexArr = [], rendererArr = [], cellCnt=[],index,noWrap,colMask;

        		tbStr.push("<table id='myTable' class='table_list' style='border-bottom:1px solid #DAE0EE'><tr class='table_list_th'>");

        		for(var o=0; o< this.columns.length; o++){//列名	

        			if(this.columns[o].orderCol != undefined){
        				
        				if($("orderCol").value == this.columns[o].orderCol||$("orderCol").value.split("-")[0]== this.columns[o].orderCol){
        					if($("order").value == '-1'){
        						colMask = "descMask";						
        					}else if($("order").value == '1'){
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
        			if((i & 1)==1){
        				tbStr.push("<tr class='table_list_row2'>");
        			}else{
        				tbStr.push("<tr class='table_list_row1'>");
        			}	
        			
        			for(var j=0;j<dataIndexArr.length;j++){	
        				
        				cellCnt = this.jsonData[i][dataIndexArr[j]];//根据dataIndex显示后台数据
        				//alert("测试222222："+this.jsonData[i][dataIndexArr[j]]);
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
        				
        				if(this.columns[j].isColor == undefined){
        		  			tbStr.push("<td style='"+ styleV +"'>" + cellCnt + "</td>");
        				}else{
        				  
        				  if(this.columns[j].isColor == 'true'){
        				  	tbStr.push("<td  style='padding:2px;background:"+cellCnt+"'>" + '&nbsp;' + "</td>");}
        				  else{
        				  	tbStr.push("<td  bgcolor='"+cellCnt+"'>" + cellCnt + "</td>");
        				  	}
        				}
        			}				
        			tbStr.push("</tr>");

        			
        		}
        				
        		tbStr.push("</table>");
        		this.container.innerHTML = tbStr.join("");
        		this.table = this.container.firstChild;

        		if(this.title != null){//表格标题	
        			var x = $('myTable').createCaption();
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
        			
        			this.table.rows[r].onmouseover = function(){ 
        				this.style.backgroundColor = createGrid.hoverColor;
        			}
        			this.table.rows[r].onmouseout = function(){ 
        				if(CurGrid.curRow!=this) this.style.backgroundColor = createGrid.backColor; 
        				else this.style.backgroundColor = createGrid.clickColor;
        			}

        			for(var c=0;c<this.table.rows[r].cells.length;c++){
        				this.table.rows[r].cells[c].onclick = function(){
        					if(CurGrid.curRow) CurGrid.curRow.style.backgroundColor = createGrid.backColor;
        					CurGrid.curRow = this.parentNode;
        					this.parentNode.style.backgroundColor = createGrid.clickColor;
        				}

        			}
        		}

        		for(var g=0; g<this.table.rows[0].cells.length;g++){
        			this.table.rows[0].cells[0].style.textAlign = 'center';//序号列居中
        			if(this.columns[g].orderCol != undefined){
        				this.table.rows[0].cells[g].onclick = function(){

        					var _order = 1;
        					if(!$("queryBtn").disabled){//亮
        						//if(CurGrid.table.rows[0].cells[this.cellIndex].innerHTML.lastIndexOf('▲')!= -1){
        						if(CurGrid.table.rows[0].cells[this.cellIndex].className == "ascMask"){
        							_order = '-1';
        						}								
        					}else{
        						if($("orderCol").value != this.cellIndex){return false;}
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
        			if($('myTable') != null){
        				removeGird(this.container);
        			}
        			myRemoteSort(orderCol,order,orderType);
        		}
        																				
        		this.load();
        	}
        }
