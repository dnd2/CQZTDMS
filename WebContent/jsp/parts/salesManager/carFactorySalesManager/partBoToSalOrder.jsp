<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件BO明细</title>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理&gt;配件销售管理&gt;BO单处理&gt;转销售单</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}"/>
<input type="hidden" name="whId" id="whId" value="${whId}"/>
<input type="hidden" name="ids" id="ids" value="${ids}"/>
<input type="hidden" name="orderId" id="orderId" value="${orderId}"/>
  <table class="table_query">
    <tr>
    <th colspan="6"><img class="nav" src="<%=contextPath%>/img/nav.gif" />BO单信息</th>
  </tr>
    <tr>
    <td   align="right"  >订货单位：</td>
      <td >
      ${dealerName}
      </td>
      <td   align="right"  >销售单位：</td>
      <td >
      	${sellerName}
      	</td>
    </tr>
    <tr>
    <td   colspan="6" align="center">
      <input class="normal_btn" type="button" value="转销售单" onclick="toSalOrder();"/>
      &nbsp;
      <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
      </td>
  </tr>
</table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type=text/javascript>
jQuery.noConflict();
autoAlertException();//输出错误信息
 var myPage;
 var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/queryPartBoDetail4Sal.json";
				
 var title = null;

 var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align:'center',width: '33px' ,renderer:seled},
				{header: "件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center',renderer:insertCodeInput},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "订货数量", dataIndex: 'BUY_QTY', align:'center'},
				{header: "满足数量", dataIndex: 'SALES_QTY', align:'center'},
				{header: "BO数量", dataIndex: 'BO_QTY', align:'center'},
				{header: "BO剩余数量", dataIndex: 'BO_ODDQTY', align:'center'},
				{header: "库存数量", dataIndex: 'NORMAL_QTY', align:'center'}
			  ];

    /*function myLink(value,meta,record){
		var boId = record.data.BO_ID;
		var stas = record.data.STATUS;
		var normalQty = record.data.NORMAL_QTY;
		if(stas=='未处理'&&normalQty&&normalQty>0){
			return String.format("<a href=\"#\" class='canSal' onclick='closeBoDtl(\""+value+"\","+boId+")'>[转销售单]</a>");
		}
		return "";
	}*/

    function seled(value,meta,record) 
	 {
		 var status = record.data.STATUS;
		 var normalQty = record.data.NORMAL_QTY;
		 var output = "";
		 output = "<input type='checkbox' value='"+value+"' name='ck' id='ck'";
         if(normalQty&&normalQty>0){
        	 output+=" class='canSal' />";
         }else{
        	 output+=" />";
         }
	 	 return output+"<input type='hidden' value='"+status+"' name='status' id='status"+value+"' />";
	 }

	function selAll(obj){
		var cks = document.getElementsByName('ck');
		for(var i =0 ;i<cks.length;i++){
			if(obj.checked){
				cks[i].checked = true;
			}else{
				cks[i].checked = false;
			}
		}
    }

	function insertCodeInput(value,meta,record){
    	var boLineId = record.data.BOLINE_ID;
        return "<input type='hidden' value='"+value+"' name='PART_OLDCODE' id='PART_OLDCODE"+boLineId+"' />"+value;
    }
    
    function insertInput(value,meta,record){
        var boLineId = record.data.BOLINE_ID;
        var stas = record.data.STATUS;
        var normalQty = record.data.NORMAL_QTY;
        if(stas=='未处理'&&normalQty&&normalQty>0){
        	return "<input type='text' class='canSal' value='"+value+"' name='REMARK"+boLineId+"' id='REMARK"+boLineId+"' />";
        }
        return value;
    }
    
    function toSalOrder(){
    	var ck = document.getElementsByName('ck');
    	var cn=0;
		for(var i = 0 ;i<ck.length; i ++){
			if(ck[i].checked){
				cn++;
                /*var sta = $("status"+ck[i].value).value;
                var code = $("PART_OLDCODE"+ck[i].value).value;
				if(sta=='已处理'){
					alert("您所选择的的配件【"+code+"】已经处理,请重新选择!");
					return;
				}*/
				
			}
		}
		if(cn==0){
			alert("请选择配件!");
			return;
		}
		MyConfirm("确定转销售单?",confirmResult);
    }
    function confirmResult(){
    	fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/toSalOrderChkInit.do";
    	fm.submit();
	}

	function goBack(){
	  window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/BOManager/boHandleQueryInit.do";
	}

	function callBack(json){
		var ps;
		//设置对应数据
		if(Object.keys(json).length>0){
			keys = Object.keys(json);
			for(var i=0;i<keys.length;i++){
			   if(keys[i] =="ps"){
				   ps = json[keys[i]];
				   break;
			   }
			}
		}
		
		//生成数据集
		if(ps.records != null){
			$("_page").hide();
			$('myGrid').show();
			new createGrid(title,columns, $("myGrid"),ps).load();			
			//分页
			myPage = new showPages("myPage",ps,url);
			myPage.printHtml();
			hiddenDocObject(2);
			jQuery(".canSal").parent("td").parent("tr").css({"background-color":"#D1FAA0"});
			//,"font-weight":"bold"
		}else{
			$("_page").show();
			$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
			$("myPage").innerHTML = "";
			removeGird('myGrid');
			$('myGrid').hide();
			hiddenDocObject(1);
		}
	}

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
			createTableHead(tbStr,dataIndexArr,rendererArr,renderParValue);
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
								tbStr.push("<tr class='table_list_row1'>");
								//rowColor = 2;
							}else
							{
								tbStr.push("<tr class='table_list_row1'>");
								//rowColor = 1;
							}
					  break;
					case  1:
					        tbStr.push("<tr class='table_list_row1'>");
							//rowColor = 2;
					  break;
					case  2:
					        tbStr.push("<tr class='table_list_row1'>");
					        //rowColor = 1;
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
										tbStr.push("<tr class='table_list_row1'>");
										//rowColor = 2;
									}else
									{
										tbStr.push("<tr class='table_list_row1'>");
										//rowColor = 1;
									}
							  break;
							case  1:
							        tbStr.push("<tr class='table_list_row1'>");
									//rowColor = 2;
							  break;
							case  2:
							        tbStr.push("<tr class='table_list_row1'>");
							       // rowColor = 1;
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
							   		}
							   		if(isNaN((cellCnt+"").replace(/\,/g,"")))//将结果替换掉逗号,以供金额显示
							   		{
							   			cellCnt = "";
							   		}
							   	    tbStr.push("<td><strong>"+cellCnt+"</strong></td>");
							   	 }    	
							}else
							{
								tbStr.push("<td></td>");
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
								tbStr.push("<tr class='table_list_row1'>");
								//rowColor = 2;
							}else
							{
								tbStr.push("<tr class='table_list_row1'>");
								//rowColor = 1;
							}
					  break;
					case  1:
					        tbStr.push("<tr class='table_list_row1'>");
							//rowColor = 2;
					  break;
					case  2:
					        tbStr.push("<tr class='table_list_row1'>");
					        //rowColor = 1;
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
				
				//this.table.rows[r].onmouseover = function(){ this.style.backgroundColor = createGrid.hoverColor; }
				//this.table.rows[r].onmouseout = function(){ 
					//if(CurGrid.curRow!=this) this.style.backgroundColor = createGrid.backColor; 
					//else this.style.backgroundColor = createGrid.clickColor;
				//}
	
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
					/*this.table.rows[r].cells[c].onclick = function()
					{
						if(CurGrid.curRow) CurGrid.curRow.style.backgroundColor = createGrid.backColor;
						CurGrid.curRow = this.parentNode;
						CurGrid.curRow.style.backgroundColor = createGrid.clickColor;
					}*/
	
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
</script>
</div>
</body>
</html>
