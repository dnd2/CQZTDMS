var myPage; // 分页对象

String.format = function() {
    if (arguments.length == 0)
        return null;
    var str = arguments[0];
    for ( var i = 1; i < arguments.length; i++) {
        var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
};

function getIndex(){}

/**
 * 公用Grid分页自定义选择每页显示记录数html生成方法
 * 
 * @author wangsw
 * @returns {String}
 */
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

/**
 * 方法说明：
 * 		查询分页，new分页对象
 * 
 * @param name
 * @param ps
 * @param url
 * @returns
 * 
 * @author infowangsw
 */
function showPages(name,ps,url) { //初始化属性
	this.name = name; //对象名称
	this.url = url; //action
	this.page = ps.curPage; //当前页数
	this.pageCount = ps.totalPages; //总页数
	this.totalRecords = ps.totalRecords; //总记录数
	this.pageSize = ps.pageSize;  // 每页条数
	this.customPageSizeFlag = ps.customPageSizeFlag; // 自定义每页条数开关
}
//丛url获得当前页数,如果变量重复只获取最后一个
showPages.prototype.getPage = function(){ 
	var args = location.search;
	var reg = new RegExp('[\?&]?' + this.argName + '=([^&]*)[&$]?', 'gi');
	var chk = args.match(reg);
	this.page = RegExp.$1;
};

//进行当前页数和总页数的验证
showPages.prototype.checkPages = function(){ 
	if (isNaN(parseInt(this.page))) this.page = 1;
	if (isNaN(parseInt(this.pageCount))) this.pageCount = 1;
	if (this.page < 1) this.page = 1;
	if (this.pageCount < 1) this.pageCount = 1;
	if (this.page > this.pageCount) this.page = this.pageCount;
	this.page = parseInt(this.page);
	this.pageCount = parseInt(this.pageCount);
};

//生成html代码
showPages.prototype.createHtml = function(){ 
	var strHtml = '', prevPage = this.page - 1, nextPage = this.page + 1;
	// 如果定义了GOLB_HIDE_HREF这个变量，则在查询的结果集只有一页的时候，不显示右下脚的分页显示信息
	if(this.pageCount>1){
		// 自定义每页条数功能
		if(this.customPageSizeFlag==true){
			strHtml += createCustomPageSizeNode();
		}
		strHtml += '<span class="count">总条数: ' + this.totalRecords + '</span>';
		strHtml += '<span class="number">';
	
		if (prevPage >= 1) {
			strHtml += '<span title="Prev Page"><a href="javascript:' + this.name + '.toPage(' + prevPage + ');">上一页</a></span>';
		}
		if (this.page < 5 && this.page > 1) {
			strHtml += '<span title="Page 1"><a href="javascript:' + this.name + '.toPage(1);">1</a></span>';
		}
		if (this.page >= 5) {
			strHtml += '<span title="Page 1"><a href="javascript:' + this.name + '.toPage(1);">1..</a></span>';
		}
		var endPage = 0;
		if (this.pageCount > this.page + 2) {
			endPage = this.page + 2;
		} else {
			endPage = this.pageCount;
		}
		
		for (var i = this.page - 2; i <= endPage; i++) {
			if (i > 0) {
			  if (i == this.page) {
				 strHtml += '<span title="Page ' + i + '"><a href="javascript:void(0)" style="color:#FF9900;background:#DDD;">' + i + '</a></span>';
			  } 
			  else if (i != 1 && i != this.pageCount) {
					strHtml += '<span title="Page ' + i + '"><a href="javascript:' + this.name + '.toPage(' + i + ');">' + i + '</a></span>';
			  }
			}
		}
		if (this.page + 3 <= this.pageCount) {
			strHtml += '<span title="Page ' + this.pageCount + '"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">..' + this.pageCount + '</a></span>';//strHtml += '<span style="border:none; background:#FFF;"><a href="javascript:void(0)">..</a></span>';
		}
		if (this.page + 3 > this.pageCount && this.page != this.pageCount) {
			strHtml += '<span title="Page ' + this.pageCount + '"><a href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">' + this.pageCount + '</a></span>';
		}
		if (nextPage > this.pageCount) {
			
		} else {
			strHtml += '<span title="Next Page"><a href="javascript:' + this.name + '.toPage(' + nextPage + ');">下一页</a></span>';
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
};

//生成页面跳转url
showPages.prototype.createUrl = function (page) { 
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
};

// 页面跳转方法
showPages.prototype.toPage = function(page){ 
  if(page>=1 && page<=this.pageCount)
  	__extQuery__(page);
};

// 打印分页html代码
showPages.prototype.printHtml = function() {
	var page = this.createHtml();
	$("#"+this.name).html(page);

	if ( ! page ) {
		$('.paging').hide();
	} else {
		$('.paging').show();
	}

	if($("#mainPageSize")) {
		$("#mainPageSize").val(this.pageSize);
	}
};

// 限定输入页数格式
showPages.prototype.formatInputPage = function(evt){ 
	var evt=evt?evt:(window.event?window.event:null);
	if(evt.keyCode == 13 && $("#pageInput").val()>=1 && $("#pageInput").val()<=this.pageCount){
		__extQuery__($("#pageInput").val());	
	}
 };


/**
 * tabArr:生成列表数组对象
 * dataArr:dataIndex数组对象
 * funcArr：render数组对象
 * 生成表头功能
 */
function createTableHead(tabArr,dataArr,funcArr,renderParArr,CurGrid)
{
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
				if($("#orderCol").val() == columnObj.orderCol||$("#orderCol").val().split("-")[0] == columnObj.orderCol)
				{
					if($("#order").val() == '-1')
					{
						colMask = "descMask";						
					}else if($("#order").val() == '1')
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
				    	if(previousColspan == count) {
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
				    }
				    else 
				    {
				    	var tableTh = "<th class='"+ colMask + "'>";
		    			tableTh += columnObj.header ;
		    			tableTh += "</th>";
				    	tabArr.push(tableTh);
				    }
				}
				else if(!columnRowspan)
		        {
		      		tabArr.push("<th class='"+ colMask + "' >" + columnObj.header + "</th>");
		        }
				    
			}
			else
			{	
				if(n==1)
				{
					if(columnRowspan)
					{
					   tabArr.push("<th class='"+ colMask +"'"+" width=" + columnObj.width +" rowspan='"+columnRowspan+"' >" + columnObj.header + "</th>");	
					}
				    else if(columnColspan)
				    {
				    	if(previousColspan == count) {
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
				    	var tableTh = "<th class='"+ colMask + "' style='word-wrap: break-word;word-break: break-all;'>";
		    			tableTh += columnObj.header ;
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
}

/**
 * 方法说明：
 * 		生成Grid数据表格
 * @param title
 * 		表格名称
 * @param columns
 * 		表格列头
 * @param cnt
 * 		Grid容器
 * @param ps
 * 		数据结果集
 * @returns
 */
function createGrid(title, columns, cnt, ps){
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
 
		//grid重画模块
		this.load = function(){
			cnt.empty(); // 清空Grid容器内容
			
			var tbStr = [], dataIndexArr = [], rendererArr = [],renderParValue = [], cellCnt=[],index,noWrap,colMask;
			
			// 生成数据列头
			createTableHead(tbStr,dataIndexArr,rendererArr,renderParValue,CurGrid);
			
			// 如有计算功能，获取计算配置信息
			var bindTableList,subTotalColumns,subTotalScolumns,totalColumns,totalSumMap,subTotalSumMap;
			
			try
			{
				if(calculateConfig)
				{
				    if(calculateConfig.bindTableList) {
				    	bindTableList = calculateConfig.bindTableList;
				    }
						
					var s;
					if(calculateConfig.subTotalColumns) {
						s = calculateConfig.subTotalColumns;
					}
					
					if(s && s.indexOf("|") >0)
					{
						subTotalColumns = s.split("|")[0];
						subTotalScolumns = s.split("|")[1];
					}
					if(calculateConfig.totalColumns) {
						totalColumns = calculateConfig.totalColumns;
					}
					if(totalColumns) {
						totalSumMap = new HashMap();
					}
				}
			}catch(e){
				// 抛出异常信息，避免列合计信息影响表格数据显示
			}	
			
			var rowColor = -1;
			for(var i=0; i< this.jsonData.length;i++){
				
				var rowsum = 0;
				var calculateFlag = false;
				
				switch(rowColor)
				{
					case -1:
					        if((i & 1)==1)
							{
								tbStr.push("<tr class='table_list_row1'>");
								rowColor = 2;
							}else
							{
								tbStr.push("<tr class='table_list_row1'>");
								rowColor = 1;
							}
					  break;
					case  1:
					        tbStr.push("<tr class='table_list_row1'>");
							rowColor = 2;
					  break;
					case  2:
					        tbStr.push("<tr class='table_list_row1'>");
					        rowColor = 1;
					  break;
				}
				
				var subTotalIndex = 0;

				for(var j=0;j<dataIndexArr.length;j++){	
					if(!subTotalSumMap) {
						subTotalSumMap = new HashMap();
					}
					    
					cellCnt = this.jsonData[i][dataIndexArr[j]];//根据dataIndex显示后台数据
					if(cellCnt == null || cellCnt == undefined){
						cellCnt ='';
					}
					
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
					
					if(this.columns[j].isColor == undefined) {
						if(this.columns[j].renderer) {
							if(typeof this.columns[j].renderer == 'function'){
								if(this.columns[j].renderer.name == 'getIndex'){
									tbStr.push("<td class='first-col' style='"+ styleV +"'>" + cellCnt + "</td>");
								} else {
									tbStr.push("<td style='"+ styleV +"'>" + cellCnt + "</td>");
								}
							} else {
								tbStr.push("<td style='"+ styleV +"'>" + cellCnt + "</td>");
							}
						} else {
							tbStr.push("<td style='"+ styleV +"'>" + cellCnt + "</td>");
						}
					}else{
					  var cellHide = '';
					  if(this.columns[j].isColor == 'true'){
					  	tbStr.push("<td " + cellHide + "style='padding:2px;background:"+cellCnt+"'>" + '&nbsp;' + "</td>");}
					  else{
					  	tbStr.push("<td " + cellHide + "bgcolor='"+cellCnt+"'>&nbsp;" + cellCnt + "&nbsp;</td>");
					  }
					}
					
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
				}
				tbStr.push("</tr>");
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
			tbStr.push("</table>");
			this.container.html(tbStr.join(""));
			this.table = this.container.children()[0];

//			if(this.title != null){//表格标题	
//				var x = $('#myTable')[0].createCaption();
//				x.innerHTML = "<span class='navi'>&nbsp;</span>"+this.title;
//			}

			/** 设置单元格  **/
			for(var r=1; r<this.table.rows.length;r++){
	            var firstCell = this.table.rows[r].cells[0];
	            if(firstCell && firstCell.firstChild && firstCell.firstChild.type == "checkbox" || firstCell && firstCell.firstChild && firstCell.firstChild.type == "radio")
	            	this.table.rows[r].ondblclick = new Function("doRowClick(this)");
				if(dataIndexArr[0] == undefined || rendererArr[0] == "function getIndex(){}"){//序号判断
					if(this.curPage == 1){//计算序号
						index = r;
					}else{
						index = parseInt(this.curPage-1)*this.pageSize + r;
					}			
					this.table.rows[r].cells[0].innerHTML = index; 
					this.table.rows[r].cells[0].style.textAlign = 'center';//序号单元格居中			
				}
				// row是否被点击过,如果点击过,mouseout时,用另一种方式变颜色
				var clickFlag = "clickFlag";
//				this.table.rows[r].click = function(){
//					
//				};
				for(var c=0;c<this.table.rows[r].cells.length;c++)
				{
				    var cell = this.table.rows[r].cells[c];
				    var cellHTML = cell.innerHTML;
//				    屏蔽td里面加载下拉菜单的html代码过长问题 add by wangsongwei
//				    if(cell && cell.innerText && typeof(cellHTML) == "string" && cellHTML.indexOf("href") == -1)
//				    {
//				        var cellText = cell.innerText;
//				        if(cellText.length)
//				        {
//				        	if(cell.showsize && cell.showsize > 0)
//				        	{
//				        		this.table.rows[r].cells[c].innerText = cellText.substr(0,showsize) + "...";
//				        		this.table.rows[r].cells[c].title = cellText;
//				        	}else if(cellText.length > 40)
//				        	{
//				        		this.table.rows[r].cells[c].innerText = cellText.substr(0,40) + "...";
//				        		this.table.rows[r].cells[c].title = cellText;
//				        	}
//				        }	
//				    }
					this.table.rows[r].cells[c].ondblclick = function()
					{

						if(CurGrid.curRow) {
							CurGrid.curRow.style.backgroundColor = CurGrid.curRow.getAttribute("oldColor");
						}
						
						CurGrid.curRow = this.parentNode;
						CurGrid.curRow.style.backgroundColor = createGrid.clickColor;
						CurGrid.curRow.setAttribute(clickFlag,"true");
					};
				}
			}
		};
	}

/**
 * 方法说明：
 * 		grid数据分页回调函数
 */
function pageBack(json) {
	try {
		// checkVals; checkParams; 翻页复选标志
		// tableSortflag; 列头排序标志
		// resizeColumnWidthFlag; 列宽调整标志
		// swapColumnFlag; 列交换标志
		// tableSortflag
		// resizeColumnWidthFlag
		// swapColumnFlag
		var ps;
		var customPageSizeFlag; //  自定义每页显示条数
		// 设置对应数据
		if (Object.keys(json).length > 0) {
			keys = Object.keys(json);
			for ( var i = 0; i < keys.length; i++) {
				if (keys[i] == "ps") {
					ps = json[keys[i]];
				}
				// 自定义每页条数
				if (keys[i] == "customPageSizeFlag") {
					customPageSizeFlag = json[keys[i]];
				}
			}
		}
		// 生成数据集
		if (ps.records != null) {
			if (customPageSizeFlag) {
				ps.customPageSizeFlag = customPageSizeFlag;
			}
			$("#_page").hide();
			$('#myGrid').show();
			$('.paging').show();
			var title = title || '';

			new createGrid(title, columns, $("#myGrid"), ps).load();

			// 分页
			myPage = new showPages("myPage", ps, url);
			myPage.printHtml();
			// hiddenDocObject(2);
			// initCheckboxChecked(checkVals, columns);
			// setGridCss();
			//__initGridHover();
			
		} else {
			$("#_page").show();
			$("#_page").html("<div class='pageTips'>没有满足条件的数据</div>");
			$("#myPage").empty();
			$('#myGrid').empty();
			$('.paging').hide();
		}
	} catch (e) {
		if ( layer && layer.msg ) {
			layer.msg(e.message + " : " + e.lineNumber, {icon: 2});
		} else {
			alert(e.message + " : " + e.lineNumber);
		}
	}
}

// 初始化table选中效果
jQuery(function($){
	$('#myGrid').on('mouseover', '.table_list_row1', function() {
		$(this).addClass('datagrid-row-hover');
	}).on('mouseout', '.table_list_row1', function() {
		$(this).removeClass('datagrid-row-hover');
	}).on('dblclick', 'tr', function() {
		var $this = $(this);
		if ($this.hasClass('datagrid-row-selected')) {
			$this.removeClass('datagrid-row-selected');
		} else {
			$this.addClass('datagrid-row-selected');
		}
	});
});
