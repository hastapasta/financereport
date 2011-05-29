package org.vikulin.runtime;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class Configuration {

	private static final Logger log = Logger.getLogger(Configuration.class);
	private PropertiesConfiguration configuration;
	private String serverFileDirectory;
	private int serverPort;
	private String serverHost;
	private int chunkSize;
	private static boolean client = false;
	
	public static void setClientServer(boolean client) {
		Configuration.client = client;
	}

	private static Configuration _instance = null;

	private Configuration() {
	}

	public static synchronized Configuration getInstance(String conf) {
		if (_instance == null) {
			_instance = new Configuration();
			_instance.readConfiguration(conf);
		}
		return _instance;
	}

	public void readConfiguration(String conf) {
		System.out.println("here 1");
		configuration = new PropertiesConfiguration();
		System.out.println("here 2");
		URL config = null;
		try {
			
			if (Configuration.client == false)
			{
				System.out.println("Using serverconf.properties");
				//config = new URL("file:./conf/serverconf.properties");
				config = new URL("file:../conf/serverconf.properties");
			}
			else
			{
				System.out.println("Using clientconf.properties");
				//config = new URL("file:./conf/clientconf.properties");
				config = new URL("file:../conf/clientconf.properties"); 
			}
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		configuration.setDelimiterParsingDisabled(true);
		log.info("---------------------- Server info ----------------------");
		try {
			configuration.load(config);
			log.info(String.format("Properties file %s", config.toString()));
		} catch (Exception e1) {
			if (config == null) {
				config = Configuration.class.getClassLoader().getResource(
						"conf.properties");
			}
			try {
				configuration.load(config);
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
			log.info(String.format("Properties file %s", config.toString()));
		}
		serverFileDirectory = configuration.getString("file.dir",
				System.getProperty("java.io.tmpdir"));
		log.info("File dir = " + getFileDirectory());

		
		serverPort = configuration.getInt("server.port");
		log.info("Server port = " + serverPort);

		serverHost = configuration.getString("server.host", "localhost");
		log.info("Server host = " + serverHost);

		chunkSize = configuration.getInt("chunk.size");
		log.info("Chunk size = " + chunkSize);

		log.info("---------------------------------------------------------");
	}

	public String getFileDirectory() {
		return (serverFileDirectory.endsWith("\\") ? serverFileDirectory
				.substring(0, serverFileDirectory.length() - 1)
				: serverFileDirectory);
	}
	

	public int getServerPort() {
		return serverPort;
	}

	public String getServerHost() {
		return serverHost;
	}

	public int getChunkSize() {
		return chunkSize;
	}

}
