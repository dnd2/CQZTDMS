/*
用于生成表格及样式
*/
function InfoTable(){
	this.tabObj=document.createElement("table");
	this.dataSet = null;
	this.headAry = null;
	this.colAry = null;
}
InfoTable.prototype.setDataSource=function(dataSet){
	this.dataSet = dataSet;
}
InfoTable.prototype.setHeadInfo=function(headAry){
	this.headAry = headAry;
}
InfoTable.prototype.setColInfo=function(colAry){
	this.colAry = colAry;
}
InfoTable.prototype.toElement=function(){
	if( this.dataSet && this.dataSet.constructor==Array && this.dataSet.length>0 
		&& this.colAry && this.colAry.constructor==Array && this.colAry.length>0 ){
		for(var i=0;i<this.dataSet.length;i++){
			var row = this.tabObj.insertRow(this.tabObj.rows.length);
			var keys =new Array(0);
			var vals =new Array(0);	
			for(var atr in this.dataSet[i] ){//取当前记录的key和val
				keys[keys.length] = atr;
				vals[keys.length-1] = eval('this.dataSet[i].'+atr);
			}			
			//alert(keys+'=='+vals);
			for(var j=0;j<this.colAry.length;j++){//格式化每一列
				var pattern = this.colAry[j];				
				for(var idx=0;idx<keys.length;idx++){//将每个有效参数名替换成该参数的索引
					pattern=pattern.replace(eval("/{"+keys[idx]+"(?=(,(number|choice|date),(\\S)*)?})/g"),'{'+idx);
					//alert(pattern);
				}
				pattern = pattern.replace(/{[^\d]+(,(number|choice|date),(\S)+)?}/g,'');//将无效参数剔除
				
				var tmp = '';
				for(var idx=0;idx<vals.length;idx++){//链接参数
					tmp=tmp.concat(',\"'+vals[idx]+'\"');
				}
				tmp = eval("messageFormat(\""+pattern+"\""+tmp+")"); //使用messageFormat格式化。
				row.insertCell(j).innerHTML=tmp;
			}
		}
	}	
	if( this.headAry && this.headAry.constructor==Array && this.headAry.length>0){
		var row= this.tabObj.createTHead().insertRow(0);
		for(i=0;i<this.headAry.length;i++){
			var ele = document.createElement('th');
			ele.innerHTML = this.headAry[i];
			row.appendChild(ele);
		}
	}
	return this.tabObj;
}
InfoTable.prototype.toString=function(){
	return 'no implement';
}

//===========================================================================
//===========================================================================
function TableStyle(attObj){
	this.keys = new Array(0);
	this.vals = new Array(0);
	if ( attObj && typeof(attObj)=='object' ){
		for(atr in attObj){
			this.keys[this.keys.length]=atr;
			this.vals[this.keys.length-1]=eval('attObj.'+atr);
		}
	}	
	this.th =null;
	this.trs=new Array(0);
}
TableStyle.prototype.setHeadStyle=function(thStyle){
	if ( thStyle instanceof TrStyle ){
		this.th = thStyle;
	}else{
		alert('TableStyle.setHeadStyle() invalid parameters :[thStyle='+thStyle+']');
	}
}
TableStyle.prototype.setTrStyle=function(idx, trStyle){
	if ( (trStyle instanceof TrStyle) && isInteger(idx) ){
		this.trs[idx] = trStyle;
	}else{
		alert('TableStyle.setTrStyle() invalid parameters :[idx='+idx+',trStyle='+trStyle+']');
	}
}
TableStyle.prototype.applyStyle=function(tableEle){
	try{
		if ( tableEle ){
			for(var i=0;i<this.keys.length;i++){//设置table属性
				_setAtt(tableEle, this.keys[i],this.vals[i]);			
			}
			
			if ( this.th && tableEle.getElementsByTagName('thead').length>0 ){//设置表头属性
				var thead = tableEle.getElementsByTagName('thead')[0];
				if ( thead.getElementsByTagName('tr').length>0 ){
					this.th.applyStyle(thead.getElementsByTagName('tr')[0]);
				}
			}
			
			//设置每行的属性
			if ( this.trs.length>0 && tableEle.getElementsByTagName('tr').length>0 ){
				var eles = tableEle.getElementsByTagName('tr');
				for(var i=0; i<eles.length ;i++ ){
					var nodeName = eles[i].parentNode.nodeName.toLowerCase();
					if ( nodeName=='table' || nodeName=='tbody' ){
						if(this.trs[i%this.trs.length]) this.trs[i%this.trs.length].applyStyle(eles[i]);
					}
				}
			}
		}
		return tableEle;
	}catch(e){
		alert(e.name+'='+e.message);
	}
}

//=======================================
function TrStyle(attObj){
	this.keys = new Array(0);
	this.vals = new Array(0);
	if ( attObj && typeof(attObj)=='object' ){
		for(atr in attObj){
			this.keys[this.keys.length]=atr;
			this.vals[this.keys.length-1]=eval('attObj.'+atr);
		}
	}
	this.tds= new Array(0);
}
TrStyle.prototype.setTdStyle=function(idx,tdStyle){
	if ( (tdStyle instanceof TdStyle) && isInteger(idx) ){
		this.tds[idx] = tdStyle;
	}else{
		alert('TrStyle.setTdStyle() invalid parameters :[idx='+idx+',tdStyle='+tdStyle+']');
	}
}
TrStyle.prototype.applyStyle=function(tr){
	if ( tr && tr.nodeName.toLowerCase()=='tr' ){
		for(var i=0;i<this.keys.length;i++){
			_setAtt(tr, this.keys[i],this.vals[i]);		
		}
	}
	if ( this.tds.length>0 && (tr.getElementsByTagName('td').length>0 
								||tr.getElementsByTagName('th').length>0) ){					
		var li = tr.getElementsByTagName('td');
		if ( li=='undefined' || li.length==0 ) li=tr.getElementsByTagName('th')
		
		for(var i=0;i<li.length; i++){
			if (this.tds[i%this.tds.length]) this.tds[i%this.tds.length].applyStyle(li[i]);
		}
	}
}
//=======================================
function TdStyle(attObj){
	this.keys = new Array(0);
	this.vals = new Array(0);
	if ( attObj && typeof(attObj)=='object' ){
		for(var atr in attObj){
			this.keys[this.keys.length]=atr;
			this.vals[this.keys.length-1]=eval('attObj.'+atr);
		}
	}
}
TdStyle.prototype.applyStyle=function(td){
	if ( td && ( td.nodeName.toLowerCase()=='td' || td.nodeName.toLowerCase()=='th' ) ){
		for(var i=0;i<this.keys.length;i++){
			_setAtt(td, this.keys[i],this.vals[i]);			
		}
	}
}
function _setAtt(ele,key,val){//设置element的属性
	if( key=='undefined' || key=='' || val=='undefined' ) return ;
	if ( 'css'==key.toLowerCase() ){
		ele.className=val;
	}else{
		ele.setAttribute(key,val);
	}
}

//加入万显示
function addWan(value){
     return String.format(
     		value+"万");
 }