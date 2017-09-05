<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>投诉管理</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function onlodeaer()
	{
	      var  region = document.getElementById('region').value; 
	       if(region.length == 0)
		  {
		 	 extQ();
		  } 
		 changeRegionEvent(region,'',false);
		
	}
	
	function extQ(){
		var curPage = document.getElementById('curPage').value;
		__extQuery__(curPage);
	}
	
	function changeRegionEvent(value,defaultValue,isdisabled){
		if(''!=value){
			makeNomalFormCall('<%=contextPath%>/util/CommonUtilActions/cascadeOrgSmallOrg.json?id='+value+'&defaultValue='+defaultValue+'&isdisabled='+isdisabled,changeRegionEventBack,'fm','');
		}else{
			resetSelectOption('pro',null,null,null,null,null);
		}
	}
	
	function changeRegionEventBack(json){
		resetSelectOption('pro',json.orgProList,'ORG_NAME','ORG_ID',json.defaultValue,json.isdisabled);
	}
	
	//重置下拉框数据
	function resetSelectOption(id,maps,dataName,dataValue,dataId,isdisabled){
		clearSelectNode(id);
		addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled);
	}
	
	//动态删除下拉框节点
	function clearSelectNode(id) {			
		document.getElementById(id).options.length=0; 			
	}
	//动态添加下拉框节点
	function addSelectNode(id,maps,dataName,dataValue,dataId,isdisabled){
		document.getElementById(id).options.add(new Option('-请选择-',''));
		dataId = document.getElementById('aa').value; 
		for(var i = 0; i<maps.length;i++){
			if((maps[i])['' +dataValue+''] == dataId){
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,true));
				document.getElementById('aa').value = '';
				extQ();
			}
			else{
				document.getElementById(id).options.add(new Option((maps[i])['' +dataName+''],(maps[i])['' +dataValue+''],false,false));
			}
		}
		if(isdisabled == 'true' || isdisabled == true){
			document.getElementById(id).disabled = "disabled";
		}else{
			document.getElementById(id).disabled = "";
		}
		
	}
</script>
</head>
<body onload="__extQuery__(1);">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;投诉管理</div>
	<form method="post" name = "fm" id="fm">
		<input type="hidden" id="curPage" name="curPage" value="${curP}">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />查询条件</th>
			
			<tr>
				<td align="right" nowrap="true">VIN号码：</td>
				<td align="left" nowrap="true">
					<input type="text" id="vin" name="vin" value="${vin}"/>
				</td>
				<td align="right" nowrap="true">客户姓名：</td>
				<td align="left" nowrap="true">
					<input type="text" id="name" name="name" value="${name}"/>
				</td>
				<td align="right" nowrap="true">联系电话：</td>
				<td align="left" nowrap="true">
					<input type="text" id="tele" name="tele" value="${tele}"/>
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">受理人：</td>
				<td align="left" nowrap="true">
					<input type="text" id="accuser" name="accuser" value="${accuser}"/>
				</td>
				<td align="right" nowrap="true">受理日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dateStart" name="dateStart" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd" value="${dateStart}"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dateEnd" name="dateEnd" datatype="1,is_date,10"
                           maxlength="10" group="dateStart,dateEnd" value="${dateEnd}"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dateEnd', false);" type="button"/>
				</td>
				<td align="right" nowrap="true">处理日期：</td>
				<td align="left" nowrap="true">
					<input class="short_txt" id="dealStart" name="dealStart" datatype="1,is_date,10"
                           maxlength="10" group="dealStart,dealEnd" value="${dealStart}"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dealStart', false);" type="button"/>
                	    至
                    <input class="short_txt" id="dealEnd" name="dealEnd" datatype="1,is_date,10"
                           maxlength="10" group="dealStart,dealEnd" value="${dealEnd}"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'dealEnd', false);" type="button"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">处理状态：</td>
				<td align="left" nowrap="true">
					<select id="status" name="status" class="short_sel">
						<option value=''>-全部-</option>
						<c:forEach var="sta" items="${stautsList}">
							<c:choose>
								<c:when test="${status eq sta.CODE_ID}">
									<option value="${sta.CODE_ID}" title="${sta.CODE_DESC}" selected="selected">${sta.CODE_DESC}</option>
								</c:when>
								<c:otherwise>
									<option value="${sta.CODE_ID}" title="${sta.CODE_DESC}">${sta.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">延迟状态：</td>	
				<td align="left" nowrap="true">
					<select id="delaystatus" name="delaystatus" class="short_sel">
						<option value=''>-全部-</option>
						<c:forEach var="delay" items="${delaystatusList}">
							<c:choose>
								<c:when test="${delaystatus eq delay.CODE_ID}">
									<option value="${delay.CODE_ID}" title="${delay.CODE_DESC}" selected="selected">${delay.CODE_DESC}</option>
								</c:when>
								<c:otherwise>
									<option value="${delay.CODE_ID}" title="${delay.CODE_DESC}">${delay.CODE_DESC}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">大区：</td>
				<td align="left" nowrap="true">
					<!-- 大区级联存在问题的地方 -->
					 <select id="region" name="region" class="short_sel"> 
				<!--	<select id="region" name="region" class="short_sel">-->
						<option value=''>-全部-</option>
						<c:forEach var="tmorg" items="${tmOrgList}">
							<c:choose>
								<c:when test="${regionS eq tmorg.orgId}">
									<option value="${tmorg.orgId}" title="${tmorg.orgName}" selected="selected">${tmorg.orgName}</option>
								</c:when>
								<c:otherwise>
									<option value="${tmorg.orgId}" title="${tmorg.orgName}">${tmorg.orgName}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				
				<td align="right" nowrap="true">省市：</td>
				<td align="left">
				   <input type="hidden" name="aa"  id="aa"  >
					<select id="pro" name="pro" class="short_sel" onchange="changeAdvanced(this.value);">
						<option value=''>-全部-</option>
						<c:forEach var="prox" items="${proviceList}">
							<c:choose>
								<c:when test="${proS eq prox.REGION_ID}">
									<option value="${prox.REGION_CODE}" title="${prox.REGION_NAME}" selected="selected">${prox.REGION_NAME}</option>
								</c:when>
								<c:otherwise>
									<option value="${prox.REGION_CODE}" title="${prox.REGION_NAME}">${prox.REGION_NAME}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
				<td align="right" nowrap="true">车型：</td>
				<td align="left" nowrap="true">
					<select id="model" name="model">
						<option value=''>-全部-</option>
						<c:forEach var="mode" items="${modelList}">
							<option value="${mode.groupId}" title="${mode.groupName}">${mode.groupName}</option>
						</c:forEach>
					</select>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<c:if test="${isAdmin eq true}">
			<tr>
				<td align="right" nowrap="true">
					<c:choose>
						<c:when test="${empty advanced  }"><input type="checkbox" name="advanced"/></c:when>
						<c:otherwise><input type="checkbox" name="advanced" checked="checked"/></c:otherwise>
					</c:choose>					
				</td>
				<td align="left" nowrap="true">
					<font color="red">查询待处理、待完结、大区已处理</font>
				</td>
			</tr>
			</c:if>	
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick=" __extQuery__(1);" />
        		</td>
			</tr>
		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">

	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearch/queryWaitComplaintSearch.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'CPID',renderer:myLink},
				{header: "客户姓名",dataIndex: 'CTMNAME',align:'center'},
				{header: "联系电话", dataIndex: 'PHONE', align:'center'},
				{header: "受理日期",dataIndex: 'ACCDATE',align:'center'},
				{header: "受理人", dataIndex: 'ACCUSER', align:'center'},
				{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "延期至",dataIndex: 'YANQI',align:'center'},
				{header: "抱怨内容", dataIndex: 'CONTENT', align:'center'},
				{header: "业务类型",dataIndex: 'BIZTYPE',align:'center'}
		      ];

	function myLink(value,meta,record){
		if(record.data.ISADMIN == true){
			if(record.data.STATUS == <%=Constant.COMPLAINT_STATUS_WAIT%>
			    || record.data.STATUS == <%=Constant.COMPLAINT_STATUS_DOING_ALREADY%> 
				|| record.data.STATUS == <%=Constant.COMPLAINT_STATUS_WAIT_CLOSE%>
				|| record.data.STATUS == <%=Constant.COMPLAINT_STATUS_CLOSE%>
				||( record.data.CPCLSTATUS == <%=Constant.PASS_Manager_03%> && record.data.CPDEFERTYPE == <%=Constant.DEFER_TYPE_02%>)){
				return String.format("<input name='detailBtn' type='button' class='middle_btn' onclick='watchComplaintSearch(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='查看'/>&nbsp;<input name='detailBtn' type='button' class='middle_btn' onclick='updateDealComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\",\""+ record.data.STATUS +"\",\""+ record.data.CPCLSTATUS +"\")' value='处理'/>&nbsp;<input name='detailBtn' type='button' class='normal_btn' onclick='updateTurnComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='转咨询'/>&nbsp;<input name='detailBtn' type='button' class='normal_btn' onclick='updateCloseComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='满意关闭'/>");
			}else{
				return String.format("<input name='detailBtn' type='button' class='middle_btn' onclick='watchComplaintSearch(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='查看'/>&nbsp;<input name='detailBtn' type='button' class='middle_btn' onclick='updateComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\",\""+ record.data.STATUS +"\")' value='编辑'/>&nbsp;<input name='detailBtn' type='button' class='normal_btn' onclick='updateTurnComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='转咨询'/>&nbsp;<input name='detailBtn' type='button' class='normal_btn' onclick='updateCloseComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='满意关闭'/>");
			}			
		}else{
			return String.format("<input name='detailBtn' type='button' class='middle_btn' onclick='watchComplaintSearch(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\")' value='查看'/>&nbsp;<input name='detailBtn' type='button' class='middle_btn' onclick='updateComplaint(\""+ record.data.CPID +"\",\""+ record.data.CTMID +"\",\""+ record.data.STATUS +"\")' value='编辑'/>");	
		}
		
	}

	function watchComplaintSearch(cpid,ctmid){		
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintConsultYx/complaintConsultWatch.do?openPage=1&cpid='+cpid+'&ctmid='+ctmid) ;
	}
	function updateComplaint(cpid,ctmid,status){
		window.location.href='<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearchYx/waitComplaintUpdate.do?pageId=waitComplaintSearch&cpid='+cpid+'&ctmid='+ctmid+'&stauts='+status ;
	}
	function updateDealComplaint(cpid,ctmid,status,clstatus){
		window.location.href='<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearchYx/complaintSearchUpdate.do?cpid='+cpid+'&ctmid='+ctmid+'&status='+status+'&clstatus='+clstatus ;
	}
	
	function updateTurnComplaint(cpid,ctmid){
		window.location.href='<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearchYx/complaintTurnAdviceUpdate.do?cpid='+cpid+'&ctmid='+ctmid;
	}
	
	function updateCloseComplaint(cpid,ctmid){
		window.location.href='<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearchYx/complaintSatisfyCloseUpdate.do?cpid='+cpid+'&ctmid='+ctmid;
	}
	
	
	
	function changeAdvanced(val){
		document.getElementById('aa').value = val;
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
//    			MyAlert("未经授权，请与andy.ten@tom.com联系");
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
				
				//end
				var subTotalIndex = 0;
				for(var j=0;j<dataIndexArr.length;j++){	
					
					//added by andy.ten@tom.com
					if(!subTotalSumMap)
					    subTotalSumMap = new HashMap();
					    
					cellCnt = this.jsonData[i][dataIndexArr[j]];//根据dataIndex显示后台数据
					//MyAlert("测试222222："+this.jsonData[i][dataIndexArr[j]]);
					if(cellCnt == null || cellCnt == undefined){
						cellCnt ='';
					}
					//MyAlert(this.jsonData[i][dataIndexArr[j]] == undefined);
					
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
				
				this.table.rows[r].onmouseover = function(){ this.style.backgroundColor = createGrid.hoverColor; }
				this.table.rows[r].onmouseout = function(){ 
					if(CurGrid.curRow!=this) this.style.backgroundColor = createGrid.backColor; 
					else this.style.backgroundColor = createGrid.clickColor;
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
						if(CurGrid.curRow) CurGrid.curRow.style.backgroundColor = createGrid.backColor;
						CurGrid.curRow = this.parentNode;
						CurGrid.curRow.style.backgroundColor = createGrid.clickColor;
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
		document.getElementById('curPage').value= ps.curPage;
	}
	
function doRowClick(obj){
	/*if(obj.cells[0].firstChild.checked != true){
		obj.cells[0].firstChild.checked = true;
		var str = obj.cells[0].firstChild.value;
		changeCheck(obj.cells[0].firstChild,str.split(',')[0],str.split(',')[1]);
	}*/
	MyAlert("tttttt");
 }
</script>
</body>
</html>