/**
 * A google.visualization.Query Wrapper. Sends a
 * query and draws the visualization with the returned data or outputs an
 * error message.
 *
 * DISCLAIMER: This is an example code which you can copy and change as
 * required. It is used with the google visualization API which is assumed to
 * be loaded to the page. For more info see:
 * http://code.google.com/apis/visualization/documentation/reference.html#Query
 */


/**
 * Constructs a new query wrapper with the given query, visualization,
 * visualization options, and error message container. The visualization
 * should support the draw(dataTable, options) method.
 * @constructor
 */



/*
 * Following function is for the non-flash dyngraph charting library.
 */


var DyGraphWrapper = function (query, chartdiv) {
	this.query = query;
	this.chartdiv = chartdiv;
		

};



DyGraphWrapper.prototype.sendAndDraw = function() {
	
	var query = this.query;

	var self = this;
	
	query.send(function(response) {self.handleQueryDygraph(response);});
	
	
};

DyGraphWrapper.prototype.handleQueryDygraph = function(response) {
	if (response.isError()) {
		//alert('Error in query: ' + response.getMessage() + ' ' + response.getDetailedMessage());
		$(this.chartdiv).html('Error in query: ' + response.getMessage() + ' ' + response.getDetailedMessage());
		$(this.chartdiv).css("background-color", "white");
		return;
	}
	
  // var g = new Dygraph.GVizChart(document.getElementById("chart-div"));
   //g.draw(data, {displayAnnotations: true, labelsKMB: true});
   //g.draw(response.getDataTable);
	new Dygraph.GVizChart(this.chartdiv).draw(response.getDataTable(), {
		//elementid).draw(response.getDataTable(), {
	});
	
	$(this.chartdiv).css("background-color", "white");
	
};
	

	


	
/*
 * End dyngraph section.
 */

var QueryWrapper = function(query, visualization, visOptions, errorContainer, columns, arrows) {

  this.query = query;
  this.visualization = visualization;
  this.options = visOptions || {};
  this.errorContainer = errorContainer;
  this.currentDataTable = null;
  this.currentDataView = null;
  this.hideColumns = columns;
  this.arrows = arrows;


  if (!visualization || !('draw' in visualization) ||
      (typeof(visualization['draw']) != 'function')) {
    throw Error('Visualization must have a draw method.');
  }
};


/** Draws the last returned data table, if no data table exists, does nothing.*/
QueryWrapper.prototype.draw = function() {

  if (!this.currentDataTable) {
    return;
  }
  //this.visualization.draw(this.currentDataTable, this.options);
  this.visualization.draw(this.currentDataView, this.options);
};


/**
 * Sends the query and upon its return draws the visualization.
 * If the query is set to refresh then the visualization will be drawn upon
 * each refresh.
 */
QueryWrapper.prototype.sendAndDraw = function() {
	
  var query = this.query;

  var self = this;

  query.setTimeout(120);
  query.send(function(response) {self.handleResponse(response);});

};


/** Handles the query response returned by the data source. */
QueryWrapper.prototype.handleResponse = function(response) {
	//alert('here 4');
  this.currentDataTable = null;
  
	//this.options = {};
    //this.options['title'] = 'Test Chart Title';
    //this.options['height'] = 400;
    //this.options['width'] = 1000;
    //this.options['showfilters'] = true;
    //alert(this.options['width']);
   	
  if (response.isError()) {
    this.handleErrorResponse(response);
  } 
  else {
    this.currentDataTable = response.getDataTable();
  
    
    
    if (this.arrows != null) {
    	var formatter = new google.visualization.ArrowFormat();
    	formatter.format(this.currentDataTable,this.arrows);
    }
    this.currentDataView = new google.visualization.DataView(this.currentDataTable);
    if (this.hideColumns != null)
    	this.currentDataView.hideColumns(this.hideColumns);
    this.draw(this.currentDataView,this.options);
    //this.draw();
  }
};


/** Handles a query response error returned by the data source. */
QueryWrapper.prototype.handleErrorResponse = function(response) {
	
  var message = response.getMessage();
  var detailedMessage = response.getDetailedMessage();
  if (this.errorContainer) {
    this.errorContainer.innerHTML="";
    google.visualization.errors.addError(this.errorContainer,
        message, detailedMessage, {'showInTooltip': false});
  } else {

    throw Error(message + ' ' + detailedMessage);
  }
};


/** Aborts the sending and drawing. */
QueryWrapper.prototype.abort = function() {

  this.query.abort();
};