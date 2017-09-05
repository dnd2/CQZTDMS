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
<title>配件拆合比例新增</title>

</head>
<body onunload='javascript:destoryPrototype()'" onload="checkRowNum();">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		基础信息管理 &gt; 配件基础信息维护 &gt; 配件拆合比例设置 &gt; 新增
</div>
<form method="post" name ="fm" id="fm">
	<input type="hidden" name="PART_ID" id="PART_ID" value=""/>
	<input type="hidden" name="myIndex" id="myIndex" value=""/>
  <table width="100%" border="0" class="table_edit" cellpadding="0" cellspacing="0">
    <tr>
      <th colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" />总成件信息</th>
    </tr>
    <tr>
	      <td width="10%"   align="right" >总成件编码：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE" readonly="readonly" datatype="0,is_null"/>
	      <input class="mark_btn" type="button" value="&hellip;" onclick="showPartInfo('PART_CODE','PART_OLDCODE','PART_CNAME','PART_ID','false')"/>
	      </td>
	      <td width="10%"   align="right"  >总成件件号：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text"  name="PART_CODE" id="PART_CODE" readonly="readonly"/>
	      </td>
	      <td  width="10%"  align="right" >总成件名称：</td>
	      <td width="20%"  >
	      <input class="middle_txt" type="text"  name="PART_CNAME" id="PART_CNAME" readonly="readonly"/>
	      </td>
      </tr>
  </table>
  <table  class="table_list" style="border-bottom:1px solid #DAE0EE">
  <tr>
      <th colspan="18" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />分总成件信息
        <input type="button" class="cssbutton"  name="queryBtn6" value="增加分件" onclick="insertRows();"/>
        </th>
    </tr>
  </table>
   <table id="subpart" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr class="table_list_row0">
      <!-- ><td><input type="checkbox" onclick="selectAll()"/></td> -->
      <th>序号</th>
      <th>分总成件件号</th>
      <th>分总成件编码</th>
      <th>分总成件名称</th>
      <th>拆分数量</th>
      <!-- <th>拆分成本比例</th> -->
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
        <input class="normal_btn" type="button" value="返回" name="button1" onclick="javascript:goback();"></td>
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
	if(tempRow<3){
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
	  //MyAlert(tempRow);
	  var Rows=tbl.rows;//类似数组的Rows 
	  var newRow=tbl.insertRow(tbl.rows.length);//插入新的一行 
	  if(tempRow%2==0){
		  newRow.className   = "table_list_row2";
	  }else{
		  newRow.className  = "table_list_row1";
	  }
	  var Cells=newRow.cells;//类似数组的Cells 
	  for (var i=0;i<8;i++)//每行的8列数据 
	  { 
	     //MyAlert(newRow.rowIndex + "   " + Cells.length);
	     var newCell=Rows(newRow.rowIndex).insertCell(Cells.length); 
	     newCell.align="center"; 
	     switch (i) 
	    { 
	      //case 0 : newCell.innerHTML="<tr><td align=\"center\"><input name=\"part\" type=\"checkbox\" id=\""+tempRow+"\"</td>";break;
	      case 0 : newCell.innerHTML="<td align=\"center\">"+tempRow+"</td>";break; 
	      case 1 : newCell.innerHTML="<td align=\"center\"><input class='middle_txt' name=\"SUBPART_CODE"+tempRow+"\" type=\"text\" id=\"SUBPART_CODE"+tempRow+"\" readonly=\"readonly\"></td>";break;
	      case 2 : newCell.innerHTML="<td align='center'><input class='middle_txt'name='SUBPART_OLDCODE"+tempRow+"' type='text' id='SUBPART_OLDCODE"+tempRow+"' readonly='readonly'/><INPUT id='SUBPART_ID"+tempRow+"' type='hidden' name='SUBPART_ID"+tempRow+"'/><INPUT class='mark_btn' type='button' value='...' onclick=showPartInfo('SUBPART_CODE"+tempRow+"','SUBPART_OLDCODE"+tempRow+"','SUBPART_CNAME"+tempRow+"','SUBPART_ID"+tempRow+"','false') /></td>";break;
	      case 3 : newCell.innerHTML="<td align=\"center\"><input class='middle_txt' name=\"SUBPART_CNAME"+tempRow+"\" type=\"text\" id=\"SUBPART_CNAME"+tempRow+"\" readonly=\"readonly\"></td>";break;
	      case 4 : newCell.innerHTML="<td align=\"center\"><input class='middle_txt' name=\"SPLIT_NUM"+tempRow+"\" type=\"text\" id=\"SPLIT_NUM"+tempRow+"\" /><font color='red'>*</font></td>";break;
	      //case 5 : newCell.innerHTML="<td align=\"center\"><input class='middle_txt' name=\"COST_RATE"+tempRow+"\" type=\"text\" id=\"COST_RATE"+tempRow+"\" /><font color='red'>*</font></td>";break;
	      case 5 : newCell.innerHTML="<td align=\"center\"><input class='middle_txt' name=\"REMARK"+tempRow+"\" type=\"text\" id=\"REMARK"+tempRow+"\"></td>";break;
	      case 6 : newCell.innerHTML="<td><input type=\"button\" class=\"cssbutton\" name=\"deleteBtn\" value=\"删除\" onclick='delTableRow("+tempRow+")'/></td></tr>";break;

	    } 
	  } 
	 } 


function delTableRow(rowNum){ 
	   var tbl=document.getElementById("subpart");
	    if (tbl.rows.length >rowNum){ 
	      
	       tbl.deleteRow(rowNum); 
	      for (var i=rowNum;i<tbl.rows.length;i++)
	       {
	         tbl.rows[i].cells[0].innerText=i;
	         tbl.rows[i].cells[6].innerHTML="<input type=\"button\" class=\"cssbutton\"  name=\"deleteBtn\" value=\"删除\" onclick='delTableRow("+i+")'/></td></tr>";
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
	if(!submitForm('fm')){
		return;	
	}
    
	  var rate=0;
	  var tempRow=0; 
	  var tbl=document.getElementById("subpart");
	  tempRow=tbl.rows.length; //行数
	  document.getElementById("myIndex").value = tempRow;
	  var ary = new Array();//定义一个数组用于存放分总成件id
	  for(var i=1;i<tempRow;i++){
		  var subPartObj = document.getElementById("SUBPART_OLDCODE"+i);
		  if(!subPartObj.value){
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
		  /*var rateObj = document.getElementById("COST_RATE"+i);
		  var rateValue = rateObj.value.trim();
		  if (rateValue.indexOf(".") >= 0) {
			  var pattern = /^((0\.0[1-9]{1})|(0\.[1-9]{1,2}))$/;
		        if(!pattern.exec(rateValue)){
		            MyAlert("拆分成本比例错误,请输入小于1的正小数，且小数保留精度最大为2位!");
		            rateObj.value="";
		            rateObj.focus();
		            myFlag=false;
					return;
		        }
		    }else{
		        MyAlert("拆分成本比例错误,请输入小于1的正小数,且小数保留精度最大为2位!");
		        rateObj.value="";
		        rateObj.focus();
		        myFlag=false;
				return;
		    }
		  rate+=parseFloat(rateObj.value);*/
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
		/*if(rate!=1){
			MyAlert("成本比例总和必须等于1");
			myFlag=false;
			return;
		}*/
		if(!myFlag){
            MyAlert("数据填写错误,请检查!");
            return;
          }
		if(confirm("确定提交?")){
			btnDisable();
			var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSplitManager/addPartSplitInfo.json";	
			sendAjax(url,getResult,'fm');
		}
}

function getResult(jsonObj){
	if(jsonObj!=null){
	    var success = jsonObj.success;
	    var error = jsonObj.error;
	    if(success){
	       MyAlert(success);
	       window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSplitManager/partSplitQueryInit.do';
	    }else if(error){
	    	MyAlert(error);
		}else if(exceptions){
	    	MyAlert(exceptions.message);
		}
	    btnEnable();
	}
}

//返回查询页面
function goback(){
	 window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartSplitManager/partSplitQueryInit.do';
}
</script>
</body>
</html>