<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import="com.infodms.dms.po.TtAsPackgeChangeApplyPO "%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息质改跟踪</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
<body onload="doInit()">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;质改跟踪
</div>
<form method="post" name ="fm" id="fm">
    <TABLE  class="table_query" width="100%;" >
          <tr>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">
          		车系：
          	</td>
          	<td width="15%">
          		<select name="carTieId" id="groupchexi" onchange="roadCheckBox(this);" class="short_sel">
	           		<option value="-1">-请选择-</option>
	           		<c:forEach items="${groupList}"   var="groupList" >
	           		<option value="${groupList.groupId}" >${groupList.groupCode}</option>
	           		</c:forEach>
           		</select>
          	</td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >车型：</td>
 			 <td width="15%">
	           	<select name="carTypeId"  id="groupchexing" class="short_sel">
	           		<option value="-1">-请选择-</option>
	           	</select>
 		    </td> 	
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" ></td>
 			 <td width="15%">
	           
 		    </td> 	
          </tr>
          <tr>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">底盘号：</td>
             <td width="15%">
           		<input name="vin" id="vin" value="" type="text" class="middle_txt" onblur="checkVin(this.value);" />
            </td> 
          	<td nowrap="true" width="10%"  class="table_query_2Col_label_7Letter">总装上线时间：</td>
	  		<td  width="15%" nowrap="true">
				<input name="roCreateDate" type="text" class="middle_txt" id="CREATE_DATE" readonly="readonly"/> 
				<div id="CREATE_DATE1" style="display: none;">
				  <input name="button" value=" "  type="button" class="time_ico" onclick="showcalendar(event, 'CREATE_DATE', false);" />  	
				</div>
			</td>	
          </tr>
          <tr>
			<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">修理日期：</td>
  			<td  width="15%" nowrap="true" >
				<input name="roRepairDateOne" type="text" class="middle_txt" id="RO_CREATE_DATE" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_CREATE_DATE', false);" />  	
	             &nbsp;至&nbsp; <input name="roRepairDateTwo" type="text" class="middle_txt" id="DELIVERY_DATE" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'DELIVERY_DATE', false);" /> 
			</td>	
          </tr>
          <tr>
  			<td nowrap="true"  class="table_query_2Col_label_7Letter">故障类别代码：</td>
            <td nowrap="true" >
            	<input name="malCode" readonly="readonly" id="malCode" value="" type="text" class="middle_txt" />
            	<input type="button" value="..." class="min_btn" onclick="choose(this,1);"/>
            </td>
  			<td nowrap="true"  class="table_query_2Col_label_7Letter">故障现象：</td>
            <td><input name="malName"  id="malName" type="text" class="middle_txt" readonly="readonly" />
            </td>
          </tr>
          <tr>
            <td  nowrap="true"  class="table_query_2Col_label_7Letter">零件号：</td>
            <input  type="hidden" name="partid"  id="partid"/>
            <td nowrap="true" >
            <input nowrap="true"  name="partcode" readonly="readonly" id="partcode" type="text" class="middle_txt" />
             <input type="button" value="..." class="min_btn" onclick="choose(this,2);"/>
            </td>
            <td nowrap="true"  class="table_query_2Col_label_7Letter">零件号名称：</td>
            <td><input name="partname" id="partname"  readonly="readonly" type="text" class="middle_txt" />
            </td>
          </tr>
          <tr>
          	<td nowrap="true"  class="table_query_2Col_label_7Letter">部件厂代码：</td>
            <td><input name="makerCode" id="makerCode"  type="text" class="middle_txt" />
              	<input type="button" value="..." class="min_btn" onclick="choose(this,3);"/>
            </td>
          	<td nowrap="true"  class="table_query_2Col_label_7Letter">部件厂名称：</td>
            <td><input name="makerName" id="makerName" type="text" class="middle_txt" />
            </td>
          </tr>
           <tr>
           		<td nowrap="true" class="table_query_2Col_label_7Letter">
           			备注信息：
           		</td>
           		<td colspan="4">
           			<textarea id="remark" name="remark" style="width: 85%;height: 80px;"></textarea>
           		</td>
           </tr>
           <tr>
           		<td colspan="6" nowrap="true">
           			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red;">温馨提示：零件个数的算法为除了故障件代码，其他字段作为条件的查询</span>
           		</td>
           </tr>
          <tr style="height: 50px;">
             <td align="center" colspan="6">
                <input type="button" class="normal_btn" onclick="doSave();"  style="width=8%" value="保存"/>
                &nbsp;
				<input type="button" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
    		</td>
    		</tr>
	</table>
	</form>
</body>
</html>
<script type="text/javascript" >
  function  roadCheckBox(obj){
  	var url =  "<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/groupchexitoxing.json?groupxi="+obj.value;
	makeCall(url,showRoadList,null);
  }
   function showRoadList(json){
		var obj = document.getElementById("groupchexing");
		if(json.groupList.length==0){
			obj.options.length=0
		    obj.options[0]=new Option("-请选择-",-1);
		}else{
			   obj.options[0]=new Option("-请选择-",-1);
				for(var i=0;i<json.groupList.length;i++){
				obj.options[i+1]=new Option(json.groupList[i].groupName, json.groupList[i].groupId + "");
			}
		}
	}
   
  function checkVin(val){
	var url =  "<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/checkVin.json?vin="+val;
	makeCall(url,showVin,null);
  }
  function showVin(json){
	  var obj=document.getElementById("CREATE_DATE");
	  var vin=document.getElementById("vin");
	  if(json.tm=="该底盘号不存在!"){
		  obj.focus();
		  vin.value="";
		  MyAlert("提示："+json.tm+"请自行填写上产日期！");
		}else{
			if(null==json.tm){
				MyAlert("该VIN无总装上线时间,请自行输入!");
				$("CREATE_DATE1").style.display='inline';
			}else{
		       obj.value=json.tm;
			}
		}
  }
  
  	function doSave(){
  		var msg="";
  		//var cx1=getVal("groupchexi");
  		//var cx2=getVal("groupchexing");
  		var createdate=getVal("CREATE_DATE");
  		var rocreatedate=getVal("RO_CREATE_DATE");
  		var deliverydate=getVal("DELIVERY_DATE");
  		/* var malCode=getVal("malCode");
  		var malName=getVal("malName") */;
  		var partcode=getVal("partcode");
  		var partname=getVal("partname");
  		/* var makerCode=getVal("makerCode");
  		var makerName=getVal("makerName"); */
  		var remark=getVal("remark");
  		/* if(cx1=="-1"){
  			msg+=" [车系]";
  		}
  		if(cx2=="-1"){
  			msg+=" [车型]";
  		} */
  		/* if(malCode==""){
  			msg+=" [故障类别代码] ";
  		}
  		if(malName==""){
  			msg+=" [故障现象] ";
  		} */
  		
  		if(partname==""){
  			msg+=" [零件号名称] ";
  		}
  		/* if(makerCode==""){
  			msg+=" [部件厂代码]";
  		}
  		if(makerName==""){
  			msg+=" [部件厂名称] ";
  		} */
  		if(remark!="" && remark.length>2000){
  			msg+=" [备注信息最长只能为2000个字符] ";
  		}
  		if(createdate==""){
			MyAlert("提示：请填写总装上线时间！");
			return;
  		}
		if(rocreatedate!="" && deliverydate==""){
			MyAlert("提示：修理日期请填写全！");
			return;
  		}
		if(rocreatedate=="" && deliverydate!=""){
			MyAlert("提示：修理日期请填写全！");
			return;
  		}
		
		if(partcode==""){
  			msg+=" [零件号] ";
  		}else{
  			var url =  "<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/checkpartcode.json?partcode="+partcode;
  			makeCall(url,showpartcode,null);
  	    }
		if(msg!=""){
			MyAlert("提示：需要选择和填写的字段有{"+msg+"}");
			return;
		}
	}
  	function showpartcode(json){
       if(json.succ=="1"){
           MyAlert("提示：该零件号已经添加！");
        }
       MyConfirm("是否确认保存？",saveFollow,"");
  	}
  	function saveFollow(){
  		var url='<%=contextPath%>/vehicleInfoManage/check/QualityInfoReportVerify/queFollowAdd.do';
  		fm.action=url;
		fm.method="post";
		fm.submit();
  	}
	function choose(obj,type){
		if(type==1){//故障件
			OpenHtmlWindow('<%=contextPath%>/jsp/vehicleInfoManage/apply/selectmalfunction1.jsp',800,500);
		}
		if(type==2){//零件号
			OpenHtmlWindow('<%=contextPath%>/jsp/vehicleInfoManage/apply/selectmalfunction2.jsp',800,500);
		}
		if(type==3){//部件厂（制造商）
			OpenHtmlWindow('<%=contextPath%>/jsp/vehicleInfoManage/apply/selectmalfunction3.jsp?',800,500);
		}
	}
	function selectmalfunctionBack1(malcode,malname){
		backVal("malCode",malcode);
		backVal("malName",malname);
	}
	function selectmalfunctionBack2(partid,partcode,partname){
		backVal("partid",partid);
		backVal("partcode",partcode);
		backVal("partname",partname);
	}
	function selectmalfunctionBack3(makercode,makername){
		backVal("makerCode",makercode);
		backVal("makerName",makername);
	}
	function backVal(id,val){
		document.getElementById(id).value=val;
	}
	function getVal(id){
		return document.getElementById(id).value;
	}
	
</script>
