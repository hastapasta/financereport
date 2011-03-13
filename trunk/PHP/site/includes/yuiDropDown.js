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
           { text: "Equities Most Gainers/Losers", url: rootpath + "/charts/equities/tablesample3A.php" },
           { text: "Equities Individual Charts", url: "http://360.yahoo.com" },
           { text: "Global Indexes", url: "http://360.yahoo.com" }
           
       ]
    },

    {
       id: "commodities", 
       itemdata: [
           { text: "Commodities Most Gainers/Losers", url: "http://auctions.shopping.yahoo.com" },
           { text: "Commodities Individual Charts", url: "http://auctions.shopping.yahoo.com" }
                 
       ]    
    },

    {
       id: "forex", 
       itemdata: [
           { text: "Forex Most Gainers/Losers", url: "http://fantasysports.yahoo.com" },
           { text: "Forex Individual Charts", url: "http://auctions.shopping.yahoo.com" }
       ] 
    },

    {
       id: "miscellaneous",
       itemdata: [
           { text: "Federal Reserve Balance Sheet", url: "http://downloads.yahoo.com" },
           { text: "Federal Reserve Change WOW", url: "http://downloads.yahoo.com" },
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