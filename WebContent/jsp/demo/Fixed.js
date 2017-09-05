var Create = {
  create: function() {
    return function() {
      this.initialize.apply(this, arguments);
    }
  }
}
var Fixed = Create.create();
Fixed.prototype = {
	initialize:function(Options){
		//this.table = getObj(Options.table);
		this.table = getObj("myTable");//table标识字
		//alert(this.table.rows(1).cells(1).offsetHeight);
		//加载前，不显示
		//this.table.style.visibility = "hidden";
		//this.width = Options.width;
		this.width = document.getElementById('myGrid').parentNode.scrollWidth;
		var exitsFixedColumnDiv = $('fixedColumnDiv');
		if(exitsFixedColumnDiv){
			return ;//this.width = this.width-parseInt(exitsFixedColumnDiv.style.width.replace('px',''));
		}
		//this.height = Options.height;
		this.height = document.getElementById('myGrid').scrollHeight;
		// 当表格内容超过grid的高度时,高度加20,为解决最后一条信息不显示的问题 
		if(this.height-this.table.scrollHeight<25){
			this.height = this.height + 20;
		}
		//this.borderWidth = Options.borderWidth?Options.borderWidth:2;
		this.borderWidth = 2;
		
		//this.lineHeight = Options.lineHeight?Options.lineHeight:0;
		this.lineHeight = 0;
		
		//this.rows = Options.rows?Options.rows:0;
		this.rows = 0;//默认没有行被锁定
		
		//this.cells = Options.cells?Options.cells:0;
		this.cells = Options.cells?Options.cells:2;//默认有几列被锁定
		
		this.createExtDiv();//创建div
		this.fixScollSize();
		this.createCells();
		this.createRows();
		this.deleteExcCels();
		this.createMast();
		//加载完成，显示
		this.table.style.visibility = "visible";
	},
	createExtDiv:function(){
		this.fixedDiv = $DC("div");//创建div
		this.fixedTitleDiv = $DC("div");
		this.fixedCTContent = $DC("div");
		this.fixedColumnDiv = $DC("div");
		this.fixedTableDiv = $DC("div");
		with(this){
			fixedDiv.className = "fixedDiv";//给div创建class
			fixedTitleDiv.className = "fixedTitleDiv";
			fixedCTContent.className = "fixedCTContent";
			fixedColumnDiv.className = "fixedColumnDiv";
			fixedColumnDiv.setAttribute("id",fixedColumnDiv.className);
			fixedTableDiv.className = "fixedTableDiv";
			
			fixedDiv.appendChild(fixedTitleDiv);//将fixedTitleDiv放入fixedDiv中
			fixedDiv.appendChild(fixedCTContent);
			fixedCTContent.appendChild(fixedColumnDiv);
			fixedCTContent.appendChild(fixedTableDiv);
			
			table.parentNode.appendChild(fixedDiv);
			fixedTableDiv.appendChild(table);
			fixedTableDiv.onscroll = function(){
				fixedTitleDiv.style.left=-this.scrollLeft+"px";
				for(var i = 0;i<rows;i++){
					for(var j = 0;j<cells;j++){
						fixedTitleDiv.childNodes[i].childNodes[j].style.left = this.scrollLeft+"px";
					}
				}
				fixedColumnDiv.style.top=-this.offsetTop+"px";
			}
		}
	},
	fixScollSize:function(){with(this){
		table.setAttribute("border","0");
		fixedDiv.style.width=width+"px";
		fixedDiv.style.height=height+"px";
		fixedTableDiv.style.width=(width-getFixedCellsWidth() - borderWidth)+"px";
		fixedTableDiv.style.height=(height-getFixedRowsHeight() - borderWidth)+"px";
		
	}},
	createRows:function(){ with(this){
		fixedTitleDiv.style.width=table.offsetWidth+"px";
		var newtit="";
		for(var i = 0;i<rows;i++){
			var titletr = table.rows[i];
			newtit += '<div style="width:'+table.offsetWidth+'px">';
			for(var j=0;j<titletr.cells.length;j++){ 
				ttd=titletr.cells[j];
				//alert(ttd.offsetWidth);
				newtit += "<div class='fixedTitle' style='width:"+(ttd.offsetWidth-borderWidth)+"px; float:left;height:"+(ttd.offsetHeight-borderWidth)+"px;"+(j<cells?"position:relative":"")+"'>"+ttd.innerHTML+"</div>\n"  
			}
			newtit += "</div>";
		}
		fixedTitleDiv.innerHTML=newtit;
		if(rows==0)fixedTitleDiv.style.display = "none";
	}},
	createCells:function(){ with(this){
		fixedTableDiv.style.marginLeft = fixedColumnDiv.style.width=getFixedCellsWidth()+"px";
		fixedColumnDiv.style.height=table.offsetHeight+"px";
		var newtit="";
		var ttd;
		var preHeight = 0;
		var curHeight = 0;
		var ttr2;
		var tbRows = table.rows;
		var rowsLen = tbRows.length;
		var myFixedHeight = this.height;
		var firstTop = 0;
		for(var i=rows;i<rowsLen;i++){ 
			newtit += "<div style='clear:both;'>";
			ttr2=tbRows[i];
			if(i+1<rowsLen){
				curHeight = tbRows[i+1].offsetTop - ttr2.offsetTop;
			} else {
				curHeight = ttr2.offsetHeight;
				myFixedHeight = ttr2.offsetTop + curHeight;
			}
			if(i==0){
				firstTop = ttr2.offsetTop==0?'':('top:'+ttr2.offsetTop+"px;");
			}
			curHeight = curHeight - borderWidth;
			for(var j = 0;j<cells;j++){
				ttd=table.rows[i].cells[j];
				newtit += "<div class='fixedTitle' style='float:left;width:"+(ttd.offsetWidth-borderWidth)+"px;height:"+(curHeight)+"px'>"+ttd.innerHTML+"</div>";
			}
			newtit += "</div>";
		}
		fixedColumnDiv.innerHTML=newtit;
	}},
	createMast:function(){with(this){
		var div = document.createElement("div");
		var div1 = document.createElement("div");
		//设置左下部庶罩
		setStyle(div,{
			width:fixedColumnDiv.offsetWidth+"px",
			height:"16px",
			background:"#EEEDE5",
			position:"absolute",
			top:(fixedTableDiv.offsetHeight-16)+"px"
		});
		//设置右上部庶罩
		setStyle(div1,{
			width:"18px",
			height:(getFixedRowsHeight()+borderWidth)+"px",
			background:"#EEEDE5",
			position:"absolute",
			zIndex:"1000",
			top:"0px",
			left:(fixedDiv.offsetWidth-20)+"px"
		});
		if(cells>0)fixedColumnDiv.parentNode.appendChild(div);
		if(rows>0)fixedDiv.appendChild(div1);
	}},
	deleteExcCels:function(){with(this){
		for(var i = 0;i<rows&&table.rows.length;i++){
			var ttr = table.rows[i];
			ttr.parentNode.removeChild(ttr);
		}
		table.style.width = table.offsetWidth-getFixedCellsWidth()+"px";
		for(var i=0;i<table.rows.length;i++){ 
			for(var j = cells-1;j>=0;j--){
				var ttd=table.rows[i].cells[j];
				//删除要固定的列
				//ttd.parentNode.removeChild(ttd); 
				//隐藏固定的列
				ttd.style.display = 'none';
			}
		}
	}},
	getFixedRowsHeight:function(){with(this){
		var height = 0;
		for(var i = 0;i<rows&&table.rows.length;i++){
			height += table.rows[i].offsetHeight;
		}
		return height;
	}},
	getFixedCellsWidth:function(){with(this){
		var width = 0;
		var length = table.rows.length;
		if(length > cells){
			length = cells;
		}
		for(var i = 0;i<cells;i++){
			width += table.rows[length-1].cells[i].offsetWidth;
		}
		return width;
	}}
}

function $(obj){
	if(typeof(obj)=="string"){
		obj = document.getElementById(obj);
	}
	return obj;
}

function $DC(str){
	return document.createElement(str);
}

//2015-05-08 ranke  需要冻结列使用
function frozenColumn(){
	new Fixed(Options);
}

function setStyle(o,style){
	for(property in style){
		o.style[property] = style[property];
	}
}
function getObj(o){
	if(typeof(o) == "undefined"){
		return null;
	}
	o = typeof(o) == "string"?$(o):o;
	return o;
}

function cscscs(){
	this.table = getObj("myTable");
	this.cells = Options.cells?Options.cells:2;//默认有几列被锁定
	var ttd;
	var ttr2;
	var fixedDiv = document.getElementById("fixedColumnDiv").childNodes;
	for(var i=0;i<table.rows.length;i++){ 
		ttr2=table.rows[i];
		var curRow = fixedDiv[i];
		var divCells = curRow.childNodes;
		for(var j = 0;j<cells;j++){
			ttd=table.rows[i].cells[j];
			var divCell = divCells[j];
			divCell.innerHTML = ttd.innerHTML;
		}
	}
}
