<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head.jsp" />
</head>
<body onunload='javascript:destoryPrototype()' onload="queryBrands()">
	<div class="box" id="box">
	<div class="box_content" >
		<div style="padding:5px;">
			<form id="fm" name="fm" method="post">
				<table class="table_list">
					<th nowrap="nowrap" width="15%">厂家</th>		
					<th nowrap="nowrap" width="30%"><input class="long_txt"  type="text" name="brand" /></th>		
					<th nowrap="nowrap" width="30%"><input class="normal_btn" type="button" value="查 询" onclick="queryBrands();"/></th>		
				</table>
			</form>
			<br />
			<table class="table_list" style="border-bottom:1px solid #DAE0EE">
				<th width="20%">A</th>
				<th width="20%">B</th>
				<th width="20%">C</th>
				<th width="20%">D</th>
				<th width="20%">E</th>
				<tr class="table_list_row1">
					<td style="border-left:none;" width="20%" valign="top">
					<div id="a" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="b" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="c" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="d" class="list_box2">
					</div>						
					</td>
					<td width="20%" valign="top">
					<div id="e" class="list_box2">
					</div>					
					</td>
				</tr>
			</table>
			
			<br />
			
			<table class="table_list" style="border-bottom:1px solid #DAE0EE">			
				<th width="20%">F</th>	
				<th width="20%">G</th>
				<th width="20%">H</th>
				<th width="20%">I</th>
				<th width="20%">J</th>
				<tr class="table_list_row1">
					<td style="border-left:none;" width="20%" valign="top">
					<div id="f" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="g" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="h" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="i" class="list_box2">
					</div>						
					</td>
					<td width="20%" valign="top">
					<div id="j" class="list_box2">
					</div>					
					</td>
				</tr>
			</table>
			
			<br />
			<table class="table_list" style="border-bottom:1px solid #DAE0EE">
				<th width="20%">K</th>
				<th width="20%">L</th>
				<th width="20%">M</th>
				<th width="20%">N</th>
				<th width="20%">O</th>	
				<tr class="table_list_row1">
					<td width="20%" valign="top" style="border:none;">
					<div id="k" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="l" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="m" class="list_box2">
					</div>						
					</td>
					<td width="20%" valign="top">
					<div id="n" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="o" class="list_box2">
					</div>					
					</td>
				</tr>
			</table>
			
			<br />
			<table class="table_list" style="border-bottom:1px solid #DAE0EE">
				<th width="20%">P</th>
				<th width="20%">Q</th>
				<th width="20%">R</th>
				<th width="20%">S</th>
				<th width="20%">T</th>	
				<tr class="table_list_row1">
					<td width="20%" valign="top" style="border:none;">
					<div id="p" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="q" class="list_box2">
					</div>						
					</td>
					<td width="20%" valign="top">
					<div id="r" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="s" class="list_box2">
					</div>				
					</td>
					<td width="20%" valign="top">
					<div id="t" class="list_box2">
					</div>					
					</td>
				</tr>
			</table>
			
			<br />
			
			<table class="table_list" style="border-bottom:1px solid #DAE0EE">			
				<th width="20%">U</th>	
				<th width="20%">V</th>
				<th width="20%">W</th>
				<th width="20%">X</th>
				<th width="20%">Y</th>
				<tr class="table_list_row1">
					<td style="border-left:none;" width="20%" valign="top">
					<div id="u" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="v" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="w" class="list_box2">
					</div>					
					</td>
					<td width="20%" valign="top">
					<div id="x" class="list_box2">
					</div>						
					</td>
					<td width="20%" valign="top">
					<div id="y" class="list_box2">
					</div>					
					</td>
				</tr>
			</table>
			
			<br />
			
			<table class="table_list" style="border-bottom:1px solid #DAE0EE;width:20%;">
				<th width="20%">Z</th>			
				<tr class="table_list_row1">
					<td valign="top"  style="border:none;">
					<div id="z" class="list_box2">
					</div>
					</td>
				</tr>
			</table>
					
			<br />
			
			<div id="carBox">
				<div style="margin:5px; border-bottom:1px solid #CCC;">		
					<span style="float:left"><a href="#" name="carBox">车系</a></span>
					<div style="clear:both"></div>
				</div>
				<div id="sers" class="list_box2" >
				</div>
			</div>
		</div>
	</div>
</div>	
<script>
	function queryBrands(){
		var url = "<%=contextPath%>/common/BrandSeriesMng/queryBrandsByDlr.json";
		makeFormCall(url,showBrandRs,'fm');
	}
	
	function querySeries(brandId){
		var url = "<%=contextPath%>/common/BrandSeriesMng/querySeries.json";
		makeCall(url,fillSeriesUl,{brandId:brandId});
	}
	
	var ch = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'];
	
	function showBrandRs(json){
		var val = json.brands;
		for(var i=0;i<ch.length;i++){ //填充brands列表
			fillBrandUl(val[ch[i]],ch[i]);
		}
		$("sers").innerHTML = "";
	}
	
	function fillBrandUl(brands,ch){ //填充每个brand的列表
		if(!brands){
			$(ch).innerHTML = "";
			return;
		}
		var str = "<ul>";
		for(var i=0;i<brands.length;i++){
			str += "<li><a href='#carBox' onclick='selBrand(this,"+brands[i].brandId+")'>"+brands[i].brandName+"</a></li>";
		}
		str += "</ul>";
		$(ch).innerHTML = str;
	}
	
	function fillSeriesUl(json){ //填充每个brand的列表
		var series = json.series;
		var str = "<ul>";
		for(var i=0;i<series.length;i++){
			str += "<li><input type='radio' name='carFamily' onclick='selSeries(\""+series[i].seriesId+"\",\""+series[i].seriesName+"\")' />"+series[i].seriesName+"</li>";
		}
		str += "</ul>";
		$("sers").innerHTML = str;
	}
	
	function selBrand(obj,brandId){ //选择厂家
		parentDocument.getElementById('brand').value = obj.innerHTML;
		if(parentDocument.getElementById('brand_id')){
			parentDocument.getElementById('brand_id').value = brandId;
		}
		querySeries(brandId);
	}
	
	function selSeries(id,name){ //选择车系
		parentDocument.getElementById('series').value = name;
		if(parentDocument.getElementById('series_id')){
			parentDocument.getElementById('series_id').value = id;
		}
		parent.removeDiv();
	}
	
</script>
</body>
</html>