<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 

"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />	
<title>配件转换</title>

</head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
<title>	</title>
</head>
<body onunload='javascript:destoryPrototype()'" onload="checkRowNum();">
<div class="wbox">
<div class="navigation">
	<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 > 基础信息管理 > 仓储相关信息维护 > 配件形态转换</div>
<form method="post" name ="fm" id="fm">
	<input type="hidden" name="PART_ID" id="PART_ID" value="${map.PART_ID }"/>
	<input type="hidden" name="myIndex" id="myIndex" value=""/>
  <table width="100%" border="0" class="table_edit" cellpadding="0" cellspacing="0">
    <tr>
      <th colspan="8">
      	<img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" /><span>总成件信息</span>
      	<input type="radio" name="TYPE" value="2" checked="checked"/>拆分
      	<input type="radio" name="TYPE" value="1" />合成
      </th>
    </tr>
	  <tr style="line-height: 10mm;">    
	      <td align="right" >总成件编码：</td>
	      <td >${map.PART_OLDCODE }
	      <input class="middle_txt" type="hidden" name="PART_OLDCODE" id="PART_OLDCODE" value="${map.PART_OLDCODE }" readonly="readonly" datatype="0,is_null"/>
	      </td>
	      <td align="right"  >总成件件号：</td>
	      <td>${map.PART_CODE }
	      <input class="middle_txt" type="hidden"  name="PART_CODE" id="PART_CODE" value="${map.PART_CODE }" readonly="readonly"/>
	      </td>
	      <td  align="right" >总成件名称：</td>
	      <td colspan="3">${map.PART_CNAME }
	      <input class="middle_txt" type="hidden"  name="PART_CNAME" id="PART_CNAME" value="${map.PART_CNAME }" readonly="readonly"/>
	      </td>
      </tr>
      <tr style="line-height: 10mm;">
    	  <td align="right" >库房：</td>
	      <td >${map.WH_NAME }
	      <input class="middle_txt" type="hidden"  name="WH_ID" id="WH_ID" value="${map.WH_ID }" readonly="readonly"/>
	      <input class="middle_txt" type="hidden"  name="WH_NAME" id="WH_NAME" value="${map.WH_NAME }" readonly="readonly"/>
	      </td>
	      <td  align="right" >库存数量：</td>
	      <td >${map.ITEM_QTY }
	      <input class="middle_txt" type="hidden"  name="ITEM_QTY" id="ITEM_QTY" value="${map.ITEM_QTY }" readonly="readonly"/>
	      </td>
	      <td  align="right" >可用数量：</td>
	      <td >${map.NORMAL_QTY }
	      <input class="middle_txt" type="hidden"  name="NORMAL_QTY" id="NORMAL_QTY" value="${map.NORMAL_QTY }" readonly="readonly"/>
	      </td>
	      <td  align="right" >货位编码：</td>
	      <td >${map.LOC_CODE }
	      <input class="middle_txt" type="hidden"  name="LOC_CODE" id="LOC_CODE" value="${map.LOC_CODE }" readonly="readonly"/>
	      </td>
	  </tr>
  </table><br/>
  <table  class="table_list" style="border-bottom:1px solid #DAE0EE">
  <tr>
      <th colspan="18" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />分总成件信息
        <input type="button" class="cssbutton"  name="queryBtn6" value="增加分件" onclick="insertRows();"/>
        </th>
    </tr>
  </table>
   <table id="subpart" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr class="table_list_row0">
      <th>序号</th>
      <th>分总成件件号</th>
      <th>分总成件编码</th>
      <th>分总成件名称</th>
      <th>转换数量</th>
      <th>货位编码</th>
      <th>备注</th>
      <th>操作</th>
    </tr>
  </table>
  <table width="100%" align="center">
    <tr>
      <td height="2"></td>
    </tr>
    <tr>
      <td align="center">
        <input class="normal_btn" type="button"  value="保存" id="submitbutton" name="button1" onclick="submitMyForm();">
        &nbsp;
        <input class="normal_btn" type="button" value="返回" name="button1" onclick="window.history.back();"></td>
    </tr>
    <tr>
      <td height="1"></td>
    </tr>
  </table>
</form>

<script type="text/javascript">
function checkRowNum(){
	var tempRow=0; 
	var tbl=document.getElementById("subpart");
	tempRow=tbl.rows.length; //行数
	if(tempRow<2){
		document.getElementById("submitbutton").disabled="disabled";
	}
}

function selectAll(){
	var obj = document.getElementsByName('part');
	for(var i=0; i<obj.length; i++) {
		if(obj.item(i).checked) {
			obj.item(i).checked = false;
		}else{
			obj.item(i).checked = true;
		}
	}
}

function insertRows(){ 
	  var tbl=document.getElementById("subpart");
	  var tempRow=0; 
	  tempRow=tbl.rows.length; //行数
	  if(tempRow>1){
		  document.getElementById("submitbutton").disabled="";
		  }
	  var Rows=tbl.rows;//类似数组的Rows 
	  var newRow=tbl.insertRow(tbl.rows.length);//插入新的一行 
	  if(tempRow%2==0){
		  newRow.className   = "table_list_row2";
	  }else{
		  newRow.className  = "table_list_row1";
	  }
	  var whId = document.getElementById("WH_ID").value;
	  var whName = document.getElementById("WH_NAME").value;
	  var Cells=newRow.cells;//类似数组的Cells 
	  for (var i=0;i<8;i++)//每行的8列数据 
	  { 
		 var index = newRow.rowIndex;
		 document.getElementById("myIndex").value = index;
	     var newCell=Rows(newRow.rowIndex).insertCell(Cells.length); 
	     newCell.align="center"; 
		 var text = "<input name='LOC_CODE_"+index+"' id='LOC_CODE_"+index+"' class='middle_txt' type='text' value='' onchange='checkCode(this,\""+index+"\",\""+whId+"\",\""+whName+"\");'/>";
		 text = text	+ "<input name='LOC_ID_"+index+"' id='LOC_ID_"+index+"' type='hidden' value='123' />";
		 text = text + "<input class='mini_btn' type='button' value='...' onclick='codeChoice(\""+index+"\",\""+whId+"\");'/>";
	     switch (i) 
	    { 
	      case 0 : newCell.innerHTML="<td align='center'>"+tempRow+"</td>";break; 
	      case 1 : newCell.innerHTML="<td align='center'><input class='middle_txt' name='SUBPART_CODE"+tempRow+"' type='text' id='SUBPART_CODE"+tempRow+"' readonly='readonly'></td>";break;
	      case 2 : newCell.innerHTML="<td align='center'><input class='middle_txt'name='SUBPART_OLDCODE"+tempRow+"' type='text' id='SUBPART_OLDCODE"+tempRow+"' readonly='readonly'/><INPUT id='SUBPART_ID"+tempRow+"' type='hidden' name='SUBPART_ID"+tempRow+"'/><INPUT class='mark_btn' type='button' value='...' onclick=showPartInfo('SUBPART_CODE"+tempRow+"','SUBPART_OLDCODE"+tempRow+"','SUBPART_CNAME"+tempRow+"','SUBPART_ID"+tempRow+"','false') /></td>";break;
	      case 3 : newCell.innerHTML="<td align='center'><input class='middle_txt' name='SUBPART_CNAME"+tempRow+"' type='text' id='SUBPART_CNAME"+tempRow+"' readonly='readonly'></td>";break;
	      case 4 : newCell.innerHTML="<td align='center'><input class='short_txt' name='SPLIT_NUM"+tempRow+"' type='text' id='SPLIT_NUM"+tempRow+"' /><font color='red'>*</font></td>";break;
	      case 5 : newCell.innerHTML=text;break;
	      case 6 : newCell.innerHTML="<td align='center'><input class='middle_txt' name='REMARK"+tempRow+"' type='text' id='REMARK"+tempRow+"'></td>";break;
	      case 7 : newCell.innerHTML="<td><input type='button' class='cssbutton' name='deleteBtn' value='删除' onclick='delTableRow("+tempRow+")'/></td></tr>";break;

	    } 
	  } 
	 } 
function codeChoice(id,whId){
	OpenHtmlWindow(g_webAppName +"/parts/storageManager/partDistributeMgr/PartDistributeMgr/selectLocationInit.do?loc_id="+id+"&whId="+whId,700,400);
}
function checkCode(th,partId,whId,whName){
    var loc_code = th.value;
    var url2 = g_webAppName+"/parts/storageManager/partDistributeMgr/PartDistributeMgr/checkSeatExist.json";
    var para = "LOC_CODE="+loc_code+"&PART_ID="+partId+"&whId="+whId+"&whName="+whName;
    makeCall(url2,forBack2,para);
}
function forBack2(json){
    if(json.returnValue != 1){
        var partId = json.PART_ID;
        if(partId!=""){document.getElementById("LOC_CODE_" + partId).value = "";}
        parent.MyAlert("该货位编码在仓库【"+json.whName+"】中不存在,请先维护在操作！");
    }else{
        codeSet(json.LOC_ID,json.LOC_CODE,json.LOC_CODE);
    }
}
var LOC_ID = null;
function codeSet(i,c,n){
	var v = i+","+c+","+n;
	document.getElementById("LOC_CODE_"+LOC_ID).value = c;
	document.getElementById("LOC_ID_"+LOC_ID).value = v;
}	
function delTableRow(rowNum){ 
	   var tbl=document.getElementById("subpart");
	    if (tbl.rows.length >rowNum){ 
	      
	       tbl.deleteRow(rowNum); 
	      for (var i=rowNum;i<tbl.rows.length;i++)
	       {
	         tbl.rows[i].cells[0].innerText=i;
	         tbl.rows[i].cells[6].innerHTML="<input type='button' class='cssbutton'  name='deleteBtn' value='删除' onclick='delTableRow("+i+")'/></td></tr>";
	         if(i%2==0){
			    	tbl.rows[i].className   = "table_list_row2";
				  }else{
					  tbl.rows[i].className  = "table_list_row1";
				  }      
	      }
	   }
	    if(tbl.rows.length<3){
			  document.getElementById("submitbutton").disabled="disabled";
		}
	} 

function submitMyForm(){
	var myFlag = true;
	  var rate=0;
	  var tempRow=0; 
	  var tbl=document.getElementById("subpart");
	  tempRow=tbl.rows.length; //行数
	  document.getElementById("myIndex").value = tempRow;
	  var ary = new Array();//定义一个数组用于存放分总成件id
	  for(var i=1;i<tempRow;i++){
		  var subPartObj = document.getElementById("SUBPART_OLDCODE"+i).value;
		  if(subPartObj == ""){
			  MyAlert("请选择分总成件编码!");
			  myFlag=false;
			  return;
		  }

          var partId = document.getElementById("PART_ID").value;
          var subPartId = document.getElementById("SUBPART_ID"+i).value;
          ary.push(subPartId);
          if(partId==subPartId){
              MyAlert("总成件与分总成件不能相同,请重新选择!");
              myFlag=false;
			  return;
          }
		  
		  var numObj = document.getElementById("SPLIT_NUM"+i);
		  if(!numObj.value){
			  MyAlert("请填写拆分数量!");
			  myFlag=false;
			  return;
		   }else{
			  var pattern1 = /^[1-9][0-9]*$/; 
			  if (!pattern1.exec(numObj.value)) {
			        MyAlert("拆分数量只能输入非零的正整数.");
			        numObj.value="";
			        numObj.focus();
			        myFlag = false;
			        return;
			   }
			}
	  }

	  var s = ary.join(",")+",";
	  for(var i=0;i<ary.length;i++){
    	  if(s.replace(ary[i]+",","").indexOf(ary[i]+",")>-1) {
        	  var subpartName  = $("SUBPART_CNAME"+(i+1)).value;
    		  MyAlert("分总成件：" +subpartName+" 重复,请重新选择!" );
    		  myFlag=false;
  			  return;
    	  }
      }
	  if(!myFlag){
           MyAlert("数据填写错误,请检查!");
           return;
       }
		if(confirm("确定提交?")){
			btnDisable();
			var url2 = g_webAppName + "/parts/baseManager/partsBaseManager/PartFormConvert/convert.json";
			sendAjax(url,getResult,'fm');
		}
}

function getResult(jsonObj){
	if(jsonObj!=null){
	    var success = jsonObj.success;
	    var error = jsonObj.error;
	    if(success){
	       MyAlert(success);
	      window.history.back();
	    }else if(error){
	    	MyAlert(error);
		}else if(exceptions){
	    	MyAlert(exceptions.message);
		}
	    btnEnable();
	}
}
</script>
</body>
</html>