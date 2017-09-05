var ESCAPE = 27
var ENTER = 13
var TAB = 9
var firstCtrl
var _beanid

var ToolMan = {
  events : function() {
    if (!ToolMan._eventsFactory) throw "ToolMan Events module isn't loaded";
    return ToolMan._eventsFactory
  },

  css : function() {
    if (!ToolMan._cssFactory) throw "ToolMan CSS module isn't loaded";
    return ToolMan._cssFactory
  },

  coordinates : function() {
    if (!ToolMan._coordinatesFactory) throw "ToolMan Coordinates module isn't loaded";
    return ToolMan._coordinatesFactory
  },

  drag : function() {
    if (!ToolMan._dragFactory) throw "ToolMan Drag module isn't loaded";
    return ToolMan._dragFactory
  },

  dragsort : function() {
    if (!ToolMan._dragsortFactory) throw "ToolMan DragSort module isn't loaded";
    return ToolMan._dragsortFactory
  },

  helpers : function() {
    return ToolMan._helpers
  },

  cookies : function() {
    if (!ToolMan._cookieOven) throw "ToolMan Cookie module isn't loaded";
    return ToolMan._cookieOven
  },

  junkdrawer : function() {
    return ToolMan._junkdrawer
  }

}

ToolMan._coordinatesFactory = {

  create : function(x, y) {
    // FIXME: Safari won't parse 'throw' and aborts trying to do anything with this file
    //if (isNaN(x) || isNaN(y)) throw "invalid x,y: " + x + "," + y
    return new _ToolManCoordinate(this, x, y)
  },

  origin : function() {
    return this.create(0, 0)
  },

  /*
   * FIXME: Safari 1.2, returns (0,0) on absolutely positioned elements
   */
  topLeftPosition : function(element) {
    var left = parseInt(ToolMan.css().readStyle(element, "left"))
    var left = isNaN(left) ? 0 : left
    var top = parseInt(ToolMan.css().readStyle(element, "top"))
    var top = isNaN(top) ? 0 : top

    return this.create(left, top)
  },

  bottomRightPosition : function(element) {
    return this.topLeftPosition(element).plus(this._size(element))
  },

  topLeftOffset : function(element) {
    var offset = this._offset(element) 

    var parent = element.offsetParent
    while (parent) {
      offset = offset.plus(this._offset(parent))
      parent = parent.offsetParent
    }
    return offset
  },

  bottomRightOffset : function(element) {
    return this.topLeftOffset(element).plus(
        this.create(element.offsetWidth, element.offsetHeight))
  },

  scrollOffset : function() {
    if (window.pageXOffset) {
      return this.create(window.pageXOffset, window.pageYOffset)
    } else if (document.documentElement) {
      return this.create(
          document.body.scrollLeft + document.documentElement.scrollLeft, 
          document.body.scrollTop + document.documentElement.scrollTop)
    } else if (document.body.scrollLeft >= 0) {
      return this.create(document.body.scrollLeft, document.body.scrollTop)
    } else {
      return this.create(0, 0)
    }
  },

  clientSize : function() {
    if (window.innerHeight >= 0) {
      return this.create(window.innerWidth, window.innerHeight)
    } else if (document.documentElement) {
      return this.create(document.documentElement.clientWidth,
          document.documentElement.clientHeight)
    } else if (document.body.clientHeight >= 0) {
      return this.create(document.body.clientWidth,
          document.body.clientHeight)
    } else {
      return this.create(0, 0)
    }
  },

  /**
   * mouse coordinate relative to the window (technically the
   * browser client area) i.e. the part showing your page
   *
   * NOTE: in Safari the coordinate is relative to the document
   */
  mousePosition : function(event) {
    event = ToolMan.events().fix(event)
    return this.create(event.clientX, event.clientY)
  },

  /**
   * mouse coordinate relative to the document
   */
  mouseOffset : function(event) {
    event = ToolMan.events().fix(event)
    if (event.pageX >= 0 || event.pageX < 0) {
      return this.create(event.pageX, event.pageY)
    } else if (event.clientX >= 0 || event.clientX < 0) {
      return this.mousePosition(event).plus(this.scrollOffset())
    }
  },

  _size : function(element) {
  /* TODO: move to a Dimension class */
    return this.create(element.offsetWidth, element.offsetHeight)
  },

  _offset : function(element) {
    return this.create(element.offsetLeft, element.offsetTop)
  }
}

function _ToolManCoordinate(factory, x, y) {
  this.factory = factory
  this.x = isNaN(x) ? 0 : x
  this.y = isNaN(y) ? 0 : y
}

_ToolManCoordinate.prototype = {
  toString : function() {
    return "(" + this.x + "," + this.y + ")"
  },

  plus : function(that) {
    return this.factory.create(this.x + that.x, this.y + that.y)
  },

  minus : function(that) {
    return this.factory.create(this.x - that.x, this.y - that.y)
  },

  min : function(that) {
    return this.factory.create(
        Math.min(this.x , that.x), Math.min(this.y , that.y))
  },

  max : function(that) {
    return this.factory.create(
        Math.max(this.x , that.x), Math.max(this.y , that.y))
  },

  constrainTo : function (one, two) {
    var min = one.min(two)
    var max = one.max(two)

    return this.max(min).min(max)
  },

  distance : function (that) {
    return Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2))
  },

  reposition : function(element) {
    element.style["top"] = this.y + "px"
    element.style["left"] = this.x + "px"
  }
}

var coordinates = ToolMan.coordinates()



function fire(url)
{
   

  var xmlhttp = false;
  if (window.XMLHttpRequest)
  {
       xmlhttp = new XMLHttpRequest();
  }else
       xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");

  xmlhttp.open("GET", url+'&_beanid='+_beanid+'&_timeis='+(new Date().getTime()),false);
  xmlhttp.send("")
 
  return xmlhttp.responseText
  
  

}

function echofire(url)
{
	var msg = fire(url)
	if(''!=msg)
		alert(msg)
}

function format(val,fmt)
{
   return val
}

function convert(val,fmt)
{
	return val

}

function textCtrl(name,index) {

    
	

	var editor = document.createElement('input')
    editor.type='text'
    editor.style['visibility'] = 'hidden'
    if(index)
		editor.index=index
	
	editor._id = name;

	if(index!=null)
		name = name +'_'+ index


    var view = document.getElementById(name)

    view.editor=editor
    this.showEditor = showEditor
    this.next=''

    view.ctrl=editor.ctrl=this
    
  

    //view.parentNode.appendChild(editor)
	function showEditor(event) {
		event = fixEvent(event)
		var editor = view.editor
		if(!editor)
		{
		   return true
		}


		if (editor.currentView != null) {
			editor.blur()
		}
		editor.currentView = view

		var topLeft = coordinates.topLeftOffset(view)
		topLeft.reposition(editor)
		if (editor.nodeName == 'TEXTAREA') {
			editor.style['width'] = view.offsetWidth + "px"
			editor.style['height'] = view.offsetHeight + "px"
		}

		if(!editor.fmt)
			editor.value = convert(view.innerText,editor.fmt)
        else
		    editor.value = view.innerText
		
		editor.style['left']=view.style['left']
		editor.style['top']=view.style['top']
		editor.style['width']=view.style['width']
		editor.style['height']=view.style['height']
		editor.style['visibility'] = 'visible'
		editor.style['position']='absolute'
		
		view.style['visibility'] = 'hidden'
		
		var parent = view.parentNode
		parent.removeChild(view)
		parent.appendChild(editor)
		editor.focus()
		currentCtrl = this;
	
		
		return false
	}

    view.onclick = showEditor





	view.editor.onblur = function(event) {
		event = fixEvent(event)

		var editor = event.target
		var view = editor.currentView

		if (!editor.abandonChanges) 
		{
			var val 
			if(editor.fmt)
				val = format(editor.value)
			else
				val = editor.value
			view.getElementsByTagName("p").item(0).innerText = val
		}
		editor.abandonChanges = false
		editor.style['visibility'] = 'hidden'
		editor.value = '' // fixes firefox 1.0 bug
		view.style['visibility'] = 'visible'
		var parent = editor.parentNode
		parent.removeChild(editor)
		parent.appendChild(view)
		
		
		editor.currentView = null

		return true
	}
	view.editor.onkeydown = function(event) {
		event = fixEvent(event)
		var editor = event.target
		if (event.keyCode == TAB )
		{
			return false
		}
	}
	
	function keyup(event)
	{
		event = fixEvent(event)

		var editor = event.target
		if (event.keyCode == ESCAPE) {
			editor.abandonChanges = true
			editor.blur()
			return false
		} else if (event.keyCode == TAB || event.keyCode==ENTER ) {

			

            fireChange(editor._id,editor.index,editor.value)
			editor.blur()
	    	 if(!editor.ctrl.next)
			{
			 
	
			   firstCtrl.showEditor(null)
			 }
			else
			   editor.ctrl.next.showEditor(null)			   
			return false

		} else {
			return true
		}
	}

	

	view.editor.onkeyup = keyup 
	// TODO: this method is duplicated elsewhere
	function fixEvent(event) {
		if (!event) event = window.event
		if (event.target) {
			if (event.target.nodeType == 3) event.target = event.target.parentNode
		} else if (event.srcElement) {
			event.target = event.srcElement
		}

		return event
	}


	
	
	
}
function fireChange(name,index,val)
{
	if(index)
	   fire('http://localhost:8080/jrs/form?_expr='+name+'['+index+'].update("'+val+'")')
	else
	   fire('http://localhost:8080/jrs/form?_expr='+name+'.update("'+val+'")')
}

function httpeval(expr)
{
    echofire('form?_expr='+expr)
}

function checkBoxCtrl(name) {
	var view = document.getElementById(name) 
		
	var editor = view.getElementsByTagName("input").item(0);
    this.showEditor = showEditor
	function showEditor(event) {
		event = fixEvent(event)
		editor.focus()
		return false
	}
	

	  

	function fixEvent(event) {
		if (!event) event = window.event
		if (event.target) {
			if (event.target.nodeType == 3) event.target = event.target.parentNode
		} else if (event.srcElement) {
			event.target = event.srcElement
		}

		return event
	}


	
	
	
}
