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
           { text: "US Equities Top Gainers/Losers", url: rootpath + "/charts/allassets/directtable.php?taskid=10&title=" + escape("US%20Equities%20Top%20Gainers/Losers") },
           { text: "Equities Individual Charts", url: rootpath + "/charts/other/comingsoon.php" },
           { text: "Global Indexes Top Gainers/Losers", url: rootpath + "/charts/allassets/directtable.php?taskid=6&title=" + escape("Global%20Indexes%20Top%20Gainers/Losers") }
           
       ]
    },

    {
       id: "commodities", 
       itemdata: [
           { text: "Commodity Futures Top Gainers/Losers", url: rootpath + "/charts/allassets/directtable.php?taskid=12&title=" + escape("Commodity%20Futures%20Top%20Gainers/Losers") },
           { text: "Commodity Futures Individual Charts", url: rootpath + "/charts/other/comingsoon.php" }
                 
       ]    
    },

    {
       id: "forex", 
       itemdata: [
           { text: "Forex Top Gainers/Losers", url: rootpath + "/charts/allassets/directtable.php?taskid=1&title=" + escape("Forex%20Top%20Gainers/Losers") },
           { text: "Forex Individual Charts", url: rootpath + "/charts/other/comingsoon.php" }
       ] 
    },

    {
       id: "miscellaneous",
       itemdata: [
           { text: "Federal Reserve Balance Sheet", url: rootpath + "/charts/fed/balsheet.php" },
           { text: "Federal Reserve Change WOW", url: rootpath + "/charts/other/comingsoon.php" },
           { text: "GDP Growth Estimates", url: rootpath + "/charts/gdp/motionchart.php" },
           { text: "All Assets - Inidivdual Line Charts", url: rootpath + "/charts/allassets/linechart.php?a=660" }
         
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