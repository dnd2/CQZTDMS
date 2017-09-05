<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>移库单管理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;新增移库单</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="valid" value=""/>
<input type="hidden" name="valname" value="" />
<input type="hidden" name="valcode" value="" />
<input type="hidden" name="valno" value="" />
<input type="hidden" name="code" value=""/>

<input type="hidden" name="cd" value=""/>
<input type="hidden" name="bo" value=""/>
<input type="hidden" name="at" value=""/>
<input type="hidden" name="se" value=""/>
<input type="hidden" name="te" value=""/>
  <table class="table_query">
    <tr class="cssTable" >
	    <td width="14%" align="right" valign="top" nowrap="nowrap" class="table_info_2col_label_6Letter">发运仓库：</td>
	    <td width="36%" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
	    	<span class="table_query_2Col_input">
		  		<select name="startName" id="startName" class="short_sel">
		  			<option value="">-请选择-</option>
		  			<c:if test="${list!=null}">
						<c:forEach items="${list}" var="list">
							<option value="${list.WAREHOUSE_ID }">${list.WAREHOUSE_NAME }</option>
						</c:forEach>
					</c:if>
		  		</select>
	    	</span>
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_query_2Col_label_6Letter">目的仓库：</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
	    	<span class="table_query_2Col_input">
		  		<select name="endName" id="endName" class="short_sel" onchange="selPrice(this.value)">
		  			<option value="">-请选择-</option>
		  			<c:if test="${list_aim!=null}">
						<c:forEach items="${list_aim}" var="list">
							<option value="${list.WAREHOUSE_ID }">${list.WAREHOUSE_NAME }</option>
						</c:forEach>
					</c:if>
		  		</select>
	    	</span>
	    </td>
	    </tr>
	    <tr class="cssTable" >
	    <td width="14%" align="right" valign="top" nowrap="nowrap" class="table_info_2col_label_6Letter">价格列表：</td>
	    <td width="36%" align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
	    	<span class="table_query_2Col_input" id="price">
	      		<select id="" name="" class="short_sel" onchange="changePrice()">
	      			<option value="-1">-请选择-</option>
	      		</select>
	    	</span>
	    </td>
	    <td align="right" valign="top" nowrap="nowrap" class="table_query_2Col_label_6Letter">&nbsp;</td>
	    <td align="left" valign="top" nowrap="nowrap" class="table_info_2col_input">
	    	<span class="table_query_2Col_input">
	      		<input class="normal_btn"  name="add" type="button" onclick="addMaterial()" value ="新增产品" />
	    	</span>
	    </td>
    </tr>
</table>

 <table class="table_list" id="table1" >
 	<tbody id="addTr">
    <tr align="center" class="cssTable" >
      <th width="15%" nowrap="nowrap">物料编号</th>
      <th width="15%" nowrap="nowrap">物料名称</th>
      <th width="10%" nowrap="nowrap">批次&nbsp;-&nbsp;数量</th>
      <th width="12%" nowrap="nowrap">移库数量</th>
      <th width="15%" nowrap="nowrap">单价</th>
      <th width="15%" nowrap="nowrap">总价</th>
      <th width="12%" nowrap="nowrap">目的库可用数量</th>
      <th width="10%" nowrap="nowrap">操作</th>
    </tr>
    </tbody>
     <tr align="center" class="header">
      <td nowrap="nowrap">&nbsp;</td>
      <td align="left" nowrap="nowrap"  ><strong>合计：</strong></td>
      <td nowrap="nowrap" ><a href="#" onclick="javascript:mannulChk();"></a></td>
      <td nowrap="nowrap" ><strong id="abc">0</strong></td>
      <td nowrap="nowrap">&nbsp;</td>
      <td nowrap="nowrap">&nbsp;</td>
      <td nowrap="nowrap" ></td>
      <td nowrap="nowrap" >&nbsp;</td>
    </tr>
  </table>
  <table class="table_query">
    <tr class="cssTable" >
      <td width="15%" align="left">
      		<input class="normal_btn"  name="add2" type="button" onclick="InsertSto()" value ="确认" />        
      		<input class="normal_btn"  name="add232" type="button" onclick="javascript:history.go(-1)" value ="返回"/></td>
      <td width="85%" align="left">&nbsp;</td>
    </tr>
  </table>
</form>
<script type="text/javascript">
	//价格列表联动
	function selPrice(val)
	{
		makeNomalFormCall("<%=contextPath%>/sales/oemstorage/OemStorageQuery/selPrice.json?endName="+val,shouSelPrice,'fm','queryBtn'); 
	}
	
	//价格列表联动  回调函数
	function shouSelPrice(json)
	{
		var ls = json.priceList
		if(ls.length>0)
		{
			var price = document.getElementById("price");
			var str = '';
			str += '<select id="priceId" name="priceId" class="short_sel" onchange="changePrice()">';
			for(var i=0; i<ls.length; i++)
			{
				str += '<option value="' + ls[i].PRICE_ID + '';
				str += '"';
				str += ls[i].IS_DEFAULT=='<%=Constant.IF_TYPE_YES%>'?'selected':'';
				str += ' >'
				str += ls[i].PRICE_DESC;
				str += '</option>'
			}
			str += '</select>';
			price.innerHTML = str;
		}
		else
		{
			MyAlert("此仓库没有价格列表！请联系管理员！");
		}
	}
	
	var atrId = 0;
	
	//删除一行
	function delItem(obj)
	{
		var i = obj.parentElement.parentElement.rowIndex;
		addTr.deleteRow(i);
		var rowlengTh = document.getElementById('table1').rows.length
		if(rowlengTh=2)
		{
			document.getElementById("startName").disabled = false;
			document.getElementById("endName").disabled = false;
		}
		else
		{
			document.getElementById("startName").disabled = true;
			document.getElementById("endName").disabled = true;		
		}
	}
	
	//添加物料
	function addMaterial()
	{
		if(document.getElementById("startName").value==""||document.getElementById("startName").value==null)
		{
			MyAlert("请选择发运仓库！")
			return;
		}else if(document.getElementById("endName").value==""||document.getElementById("endName").value==null)
		{
			MyAlert("请选择目的仓库！");
			return;
		}else if(document.getElementById("priceId").value==""||document.getElementById("priceId").value==null)
		{
			MyAlert("请选择一款价格！");
			return;
		}else
		{
			var stName = document.getElementById("startName").value;
			OpenHtmlWindow('<%=contextPath%>/sales/oemstorage/OemStorageQuery/addMaterial.do?stName='+stName,800,500);
		}
	}
	
	function getModel(valid,valcode,valname,valno)
	{
		document.getElementById("valid").value = valid;
		document.getElementById("valcode").value = valcode;
		document.getElementById("valname").value = valname;
		document.getElementById("valno").value = valno;
		document.getElementById("endName").disabled = false;
		makeNomalFormCall("<%=contextPath%>/sales/oemstorage/OemStorageQuery/selOthers.json",getModelBack,'fm','queryBtn'); 
	}
	
	function getModelBack(json)
	{
		if(json.map.singlePrice==null)
		{
			MyAlert("此款物料在目的经销商没有价格！请联系管理员维护！");
			return;
		}
		else if(json.map.mcount==null)
		{
			MyAlert("此款物料在目的经销商没有库存！");
		}
		else
		{
			var sp = json.map.singlePrice;
			var mc = json.map.mcount;
			addRows(sp, mc);
		}
	}
	
	function addRows(sp, mc)
	{
		atrId = atrId + 1;
		var aTr = document.createElement("TR");	
		if(atrId%2 != 0)
		{
			aTr.className  = "table_list_row1";
		}
		else
		{
			aTr.className  = "table_list_row2";
		}
		aTr.id = atrId;
		addTr.appendChild(aTr);
		var valcode = document.getElementById("valcode").value;
		var valname = document.getElementById("valname").value
		var valno = document.getElementById("valno").value
		var td1 = valcode;
		var td2 = valname;
		var td3 = valno;
		var td4 = '<input name=\"amount\" id=\"'+ atrId +'\" type=\"text\" onblur="getCount(this)" class=\"short_txt\">';
		var td5 = amountFormatNew(sp);
		var td6 = '';
		var td7 = mc;
		var td8 = '<input type=button value=\"删除\" class=\"normal_btn\" name=\"remain\" onclick=\"javascript:delItem(this)\">';

		var aTD1 = document.createElement("<TD>");
		var aTD2 = document.createElement("<TD>");
		var aTD3 = document.createElement("<TD>");
		var aTD4 = document.createElement("<TD>");
		var aTD5 = document.createElement("<TD>");
		var aTD6 = document.createElement("<TD>");
		var aTD7 = document.createElement("<TD>");
		var aTD8 = document.createElement("<TD>");
		
		aTD5.id = 'sm_' + atrId;
		aTD6.id = 'tm_' + atrId;

		aTr.appendChild(aTD1);
		aTr.appendChild(aTD2);
		aTr.appendChild(aTD3);
		aTr.appendChild(aTD4);
		aTr.appendChild(aTD5);
		aTr.appendChild(aTD6);
		aTr.appendChild(aTD7);
		aTr.appendChild(aTD8);

		aTD1.innerHTML=td1;
		aTD1.align = "center";
		aTD2.innerHTML=td2;
		aTD2.align = "center";
		aTD3.innerHTML=td3;
		aTD3.align = "center";
		aTD4.innerHTML=td4;
		aTD4.align = "center";
		aTD5.innerHTML=td5;
		aTD5.align = "center";
		aTD6.innerHTML=td6;
		aTD6.align = "center";
		aTD7.innerHTML=td7;
		aTD7.align = "center";
		aTD8.innerHTML=td8;
		aTD8.align = "center";
		
		var rowlengTh = document.getElementById('table1').rows.length
		if(rowlengTh>2)
		{
			
			document.getElementById("startName").disabled = true;
			document.getElementById("endName").disabled = true;
		}
	}
	
	function getCount(obj)
	{
		if(obj.value!=null||obj.value!=""){
			if(!isNaN(obj.value))
			{
				var str = obj.id;
				var i = obj.parentElement.parentElement.rowIndex;
				var trid = document.all.table1;
				if(trid.rows[i].id = str)
				{
					var bno = trid.rows[i].cells[2].innerHTML;
					bno = bno.substring(7);
					if(new Number(bno) - new Number(obj.value) < 0)
					{
						MyAlert("移库数量不能大于库存数量");
						obj.value = 0;
						return;
					}
					else
					{
						var sp = trid.rows[i].cells[4].innerHTML;
			    	    var t = sp.replace(/[,]/g,"");
			    		trid.rows[i].cells[5].innerHTML = amountFormatNew(t*obj.value);
			    		var cnt = 0;
			    		var aunt = document.getElementsByName("amount");

			    		for(var j=0;j<aunt.length;j++)
			    		{
			    			cnt += new Number(aunt[j].value);
			    		}
						document.getElementById("abc").innerHTML = cnt; 
					}
				}
			}
			else
			{
				MyAlert("请输入一个整数");
				obj.value = 0;
				return;
			}
		}
		else
		{
			trid.rows[i].cells[5].innerHTML = 0.00;
			return;
		}
	}
	
	function changePrice()
	{
		var code;
		var tabrow = document.getElementById('table1');
		if(tabrow.rows.length>2)
		{
			for(var i=1;i<tabrow.rows.length-1;i++)
			{
				if(code == null)
				{
					code = tabrow.rows[i].cells[0].innerHTML;
					
				}
				else
				{
					code += ',' + tabrow.rows[i].cells[0].innerHTML;
				}
			}

			document.getElementById("code").value = code;
			document.getElementById("endName").disabled = false;
			makeNomalFormCall("<%=contextPath%>/sales/oemstorage/OemStorageQuery/changePrice.json",changePriceBack,'fm','queryBtn'); 
		}
	}
	
	function changePriceBack(json)
	{
		document.getElementById("endName").disabled = true;
		if(json.singList.length>0)
		{
			var tabrow = document.getElementById('table1')
			for(var i=0;i<json.singList.length;i++)
			{
			    tabrow.rows[i+1].cells[4].innerHTML = amountFormatNew(json.singList[i].singlePrice);
			    var am = document.getElementsByName("amount");
			    if(am.value!=null||am.value!="")
			    {
			    	tabrow.rows[i+1].cells[5].innerHTML = amountFormatNew(am[i].value*json.singList[i].singlePrice);
			    }
			    else{
			    	tabrow.rows[i+1].cells[5].innerHTML = 0.00;
			    }
			}
		}
	}
	
	function InsertSto()
	{
		var tabrow = document.getElementById('table1')
		if(tabrow.rows.length>2)
		{
			var b = new Object();
			b = document.getElementById("add2");
			var a = new Object();
			a = document.getElementById("add232");
			a.disabled=true;
			b.disabled=true;
			if(confirm("确认提交？")){
				insertSto();
			}else{
				a.disabled=false;
				b.disabled=false;
			}
		}
		else
		{
			MyAlert("请添加需要移库车辆！");
			return;
		}
	}
	
	function insertSto()
	{
		var cd;
		var bo;
		var at;
		var se;
		var te;
		var tabrow = document.getElementById('table1');
		for(var i=1;i<tabrow.rows.length-1;i++)
		{
			var am = document.getElementsByName("amount");	
			if(cd == null)
			{
				cd = tabrow.rows[i].cells[0].innerHTML;
				bo = tabrow.rows[i].cells[2].innerHTML;
				at = am[i-1].value;
				se = tabrow.rows[i].cells[4].innerHTML;
				te = tabrow.rows[i].cells[5].innerHTML;
			}
			else 
			{
				cd += ',' + tabrow.rows[i].cells[0].innerHTML;
				bo += ',' + tabrow.rows[i].cells[2].innerHTML;
				at += ',' + am[i-1].value;
				se += '-' + tabrow.rows[i].cells[4].innerHTML;
				te += '-' + tabrow.rows[i].cells[5].innerHTML;
			}
		}
		
		document.getElementById("cd").value = cd;
		document.getElementById("bo").value = bo;
		document.getElementById("at").value = at;
		document.getElementById("se").value = se;
		document.getElementById("te").value = te;
		document.getElementById("startName").disabled = false;
		document.getElementById("endName").disabled = false;
		makeNomalFormCall("<%=contextPath%>/sales/oemstorage/OemStorageQuery/InsertSTO.json",insertStoBack,'fm','queryBtn'); 
	}
	
	function insertStoBack()
	{
		if(json.returnValue == '1') {
			parent.MyAlert("操作成功！");
			window.location = "<%=contextPath%>/sales/oemstorage/OemStorageQuery/storageMoveQueryInit.do";
		} else {
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
</script>
</body>
</html>
