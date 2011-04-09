function yuiCallBack() {

    /*
         Instantiate a MenuBar:  The first argument passed to the 
         constructor is the id of the element in the page 
         representing the MenuBar; the second is an object literal 
         of configuration properties.
    */

	
    var oMenuBar = new YAHOO.widget.MenuBar("productsandservices", { 
                                                autosubmenudisplay: true, 
                                                hidedelay: 750, 
                                                lazyload: true });

    /*
    Define an array of object literals, each containing 
    the data necessary to create a submenu.
    */

    var aSubmenuData = [

    {
       id: "equities", 
       itemdata: [ 
           { text: "US Equities Percent Gain/Loss (S&P500)", url: rootpath + "/charts/allassets/directtable.php?taskid=10&title=" + escape("US%20Equities%20Percent%20Gain/Loss") },
           { text: "US Equities Quarterly Earnings", url: rootpath + "/charts/equities/epslinechart3.php?entityid=1" },
           { text: "US Equities Individual Line Charts", url: rootpath + "/charts/allassets/linechart.php?e=1&group=1&title=" + escape("US%20Equities%20Indivdual%20Line%20Charts") },
           { text: "Global Indexes Percent Gain/Loss", url: rootpath + "/charts/allassets/directtable.php?taskid=6&title=" + escape("Global%20Indexes%20Percent%20Gain/Loss") }
           
       ]
    },

    {
       id: "commodities", 
       itemdata: [
           { text: "Commodity Percent Gain/Loss", url: rootpath + "/charts/allassets/directtable.php?taskid=12&title=" + escape("Commodity%20Futures%20Percent%20Gain/Loss") },
           { text: "Commodity Futures Individual Line Charts", url: rootpath + "/charts/allassets/linechart.php?e=673&group=4&title=" + escape("Commodity%20Futures%20Indivdual%20Line%20Charts") }
                 
       ]    
    },

    {
       id: "forex", 
       itemdata: [
           { text: "Forex Percent Gain/Loss", url: rootpath + "/charts/allassets/directtable.php?taskid=1&title=" + escape("Forex%20Percent%20Gain/Loss") },
           { text: "Forex Individual Line Charts", url: rootpath + "/charts/allassets/linechart.php?e=508&group=3&title=" + escape("Forex%20Indivdual%20Line%20Charts") }
       ] 
    },

    {
       id: "miscellaneous",
       itemdata: [
           { text: "Federal Reserve Balance Sheet", url: rootpath + "/charts/fed/balsheet.php" },
           { text: "Federal Reserve Change WOW", url: rootpath + "/charts/other/comingsoon.php" },
           { text: "GDP Growth Estimates", url: rootpath + "/charts/gdp/motionchart.php" },
           { text: "All Assets - Inidivdual Line Charts", url: rootpath + "/charts/allassets/linechart.php?a=660&title=" + escape("All%20Assets%20Indivdual%20Line%20Charts") }
         
       ]
    }                    
    ];


    /*
    Subscribe to the "beforerender" event, adding a submenu 
    to each of the items in the MenuBar instance.
    */

    oMenuBar.subscribe("beforeRender", function () {

    if (this.getRoot() == this) {

       this.getItem(0).cfg.setProperty("submenu", aSubmenuData[0]);
       this.getItem(1).cfg.setProperty("submenu", aSubmenuData[1]);
       this.getItem(2).cfg.setProperty("submenu", aSubmenuData[2]);
       this.getItem(3).cfg.setProperty("submenu", aSubmenuData[3]);

    }

    });


    /*
    Call the "render" method with no arguments since the 
    markup for this MenuBar instance is already exists in 
    the page.
    */

    oMenuBar.render();  

}