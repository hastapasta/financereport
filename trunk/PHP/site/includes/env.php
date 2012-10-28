<?php 

class Environment {
	
	
	static public $PHP_ROOT_PATH="";
	//static public $PHP_ROOT_PATH="/phpdev";
	//static public $PHP_ROOT_PATH="/phptest";
	
	//static public $CAKE_ROOT_PATH="/cakepfdev";
	static public $CAKE_ROOT_PATH="/cakephp";
	
	
	static public $JSP_HOST="devdataload";
	static public $JSP_PORT="8080";
	static public $JSP_PATH="JSPDataSource";
	
	static function getJSPPath($absolute) {
		if ($absolute)
			return "http://".Environment::$JSP_HOST.":".Environment::$JSP_PORT."/".Environment::$JSP_PATH."/";
		else
			return Environment::$JSP_PATH."/";

	}
	
	
	//static public $JSP_ROOT_PATH="http://devdataload:8080/JSPDataSource/";
	//static public $JSP_ROOT_PATHxx="http://devdataload:8080/JSPDataSourcexx/";
	//static public $JSP_ROOT_PATH="http://testdataload:8080/JSPDataSource/";
	//static public $JSP_ROOT_PATH="http://www.testpikefin.com/devjsp/JSPDataSource/";
	//static public $JSP_ROOT_PATH="http://www.pikefin.com/testjsp/JSPDataSource/";
	//static public $JSP_ROOT_PATH="http://192.168.122.133:8080/JSPDataSource/";
	
	static function getPHPSitePath($absolute) {
		//absolute
		if ($absolute)
			return $_SERVER[HTTP_HOST].Environment::$PHP_ROOT_PATH;
		else
			return Environment::$PHP_ROOT_PATH;
		//relative
		//return "../";
	}
	
	static function getPHPCommonPath($absolute) {
		if ($absolute)
			return $_SERVER[HTTP_HOST].Environment::$PHP_ROOT_PATH;
		else
			return Environment::$PHP_ROOT_PATH;
	}
	
	static function getPHPAjaxsamplePath($absolute) {
		if ($absolute)
			return $_SERVER[HTTP_HOST].Environment::$PHP_ROOT_PATH;
		else
			return Environment::$PHP_ROOT_PATH;
	}
	
	
	static function getPHPChartsPath($absolute) {
		if ($absolute)
			return $_SERVER[HTTP_HOST].Environment::$PHP_ROOT_PATH;
		else
			return Environment::$PHP_ROOT_PATH;
	}
	
	static function getPHPJSONPath($absolute) {
		if ($absolute)
			return $_SERVER[HTTP_HOST].Environment::$PHP_ROOT_PATH;
		else
			return Environment::$PHP_ROOT_PATH;
	}
	
	
	
	static function getCakePHPPath ($absolute) {
		if ($absolute)
			return $_SERVER[HTTP_HOST].Environment::$CAKE_ROOT_PATH;
		else
			return Environment::$CAKE_ROOT_PATH;
	
	}
	
	
}





?>