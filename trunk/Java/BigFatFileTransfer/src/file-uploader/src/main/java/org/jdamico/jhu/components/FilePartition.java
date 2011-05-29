package org.jdamico.jhu.components;

import java.io.File;

import org.apache.log4j.Logger;
import org.jdamico.jhu.dataobjects.FileMap;
import org.jdamico.jhu.dataobjects.PartialFile;
import org.jdamico.jhu.dataobjects.SourceFile;
import org.jdamico.jhu.runtime.Upload;
import org.jdamico.jhu.utils.Helper;
import org.vikulin.utils.Constants;

public class FilePartition {
	
	public FilePartition(Upload upload) {
		this.upload = upload;
	}
	private static final Logger log = Logger.getLogger(FilePartition.class);
	
	private Upload upload;

	public void start(File inputFile, int size, String host, int port)
			throws Exception {
		String sourceFileName = inputFile.getAbsolutePath();
		long t1 = System.currentTimeMillis();

		String partialElementName = null;
		Controller control = new Controller();

		int npart = size;

		File file = inputFile;

		long filesize = file.length();
		long heapMaxSize = Runtime.getRuntime().maxMemory();

		if (filesize < size) {
			System.out
					.println("The file size, is smaller than the part file size.");
		}

		String toFolder = Constants.conf.getFileDirectory()+ Constants.PATH_SEPARATOR;//file.getParent() + Constants.PATH_SEPARATOR;
		int tail = 0;
		;
		if (filesize == 0) {
			tail = 1;
		} else {
			tail = (int)(filesize % npart);
		}

		int noOfParts = (int)(filesize / npart);

		int i = 0;
		if (tail > 0) {
			noOfParts++;
		}
		int arraySize = noOfParts;
		upload.setStatus(Upload.SPLITTING);
		Splitter splitter = new Splitter(file, noOfParts, npart, false, upload);
		splitter.split(toFolder);

		PartialFile[] partialFileList = new PartialFile[arraySize];
		upload.setStatus(Upload.CALCULATION_CHECKSUMS);
		for (i = 0; i < noOfParts; ++i) {
			int percent = 100 * (i + 1) / (noOfParts);
			upload.setProgress(percent);
			partialElementName = file.getName() + ".part_" + i;
			String md5 = control.getFileMD5(toFolder + partialElementName);
			log.info(partialElementName + " | "+md5 + " [ok]");
			partialFileList[i] = new PartialFile(partialElementName, md5);

		}

		String sourceFileMD5 = control.getFileMD5(sourceFileName);
		log.info(inputFile.getName() + " | "+sourceFileMD5 +" [ok]");

		SourceFile sourceFile = new SourceFile(file.getName(), sourceFileMD5, npart);

		FileMap fileMap = new FileMap(sourceFile, partialFileList);

		control.writeFileMapXML(fileMap, toFolder);
		System.out.println("\rLocal FileMap generated.                        ");
		
		PartialFile[] sentFiles = null;
		try {
			sentFiles = control.checkSentFiles(file.getName() + ".xml", host,
					port);
		} catch (Exception e) {
			System.out
					.println("Unable to check remote FileMap at remote host.");
			upload.error(e.getMessage());
			return;
			
		}

		int remainingFiles = 0;
//		if (sentFiles == null) {
			boolean isUploaded = control.uploadFiles(new File(toFolder + file.getName() + ".xml"), null, host,
					port, upload, null);
			if (!isUploaded) return;
			System.out
					.println("Starting upload. Local FileMAP xml sent to remote host!");
//		} else {
//			System.out.println("Resuming upload.");
//			for (i = 0; i < sentFiles.length; ++i) {
//				if (!(sentFiles[i].isUploaded()))
//					continue;
//				++remainingFiles;
//			}
//		}
		try {
			sentFiles = control.checkSentFiles(file.getName() + ".xml", host, port);
		} catch (Exception e) {
			System.out
					.println("Unable to check remote FileMap at remote host.");
			upload.error(e.getMessage());
			return;
		}

		int rInit = 0;

		boolean allSent = false;

		if (remainingFiles == noOfParts)
			allSent = true;
		upload.setStatus(Upload.UPLOADING);
		boolean errorFlug = false;
		while (!(allSent)) {
			sentFiles = control.checkSentFiles(file.getName() + ".xml", host,
					port);

			isUploaded = false;
			for (i = 0; i < sentFiles.length; ++i) {
				if (!(sentFiles[i].isUploaded())) {
					partialElementName = toFolder + file.getName() + ".part_" + i;
					File partialElementFile = new File(partialElementName);
					++remainingFiles;

					isUploaded = control.uploadFiles(partialElementFile,
							file.getName() + ".xml", host, port, upload, i);
					if (isUploaded){						
						//partialElementFile.delete();
					} else {
						errorFlug=true;
					}
					Float diff = Float.valueOf((float) (System
							.currentTimeMillis() - t1) / 1000.0F);
					String time = null;

					if (diff.floatValue() < 60.0F) {
						time = Helper.getInstance().formatDecimalCurrency(diff,
								"###.##")
								+ " s";
					} else {
						Float m = Float.valueOf(diff.floatValue() / 60.0F);
						time = Helper.getInstance().formatDecimalCurrency(m,
								"####")
								+ " m";
					}
					int percent = 100 * remainingFiles / sentFiles.length;

					++rInit;
					Float transferRate = Float.valueOf(rInit * size
							/ diff.floatValue() / 1000.0F);

					float eta = (sentFiles.length - remainingFiles) * size
							/ 1000 / transferRate.floatValue()
							/ transferRate.floatValue();

					System.out.print("\rUploaded "
							+ remainingFiles
							+ "/"
							+ sentFiles.length
							+ " ["
							+ time
							+ "] "
							+ percent
							+ "% ["
							+ Helper.getInstance().formatDecimalCurrency(
									transferRate, "#####.##")
							+ " KBps] ETA: "
							+ Helper.getInstance().formatDecimalCurrency(
									Float.valueOf(eta), "#####.##")
							+ " m           ");
					upload.setProgress(percent);
				}

			}

			if (remainingFiles != noOfParts) continue;
			allSent = true;
		}

		if (!(allSent) || errorFlug)
			return;
		upload.setStatus(Upload.JOINING);
		
		log.info("Telling server to consolidate file: " + file.getName());
		control.consolidateFileAtRemoteHost(file.getName() + ".xml", host,
				port, upload);
		upload.setProgress(100);
		File rmF = new File(toFolder + file.getName() + ".xml");
		rmF.delete();
		System.out.println("\nFile " + file.getName() + " Sent!");

		upload.setStatus(Upload.COMPLETE);
		upload.setProgress(100);
	}

}

/*
 * Location: D:\Users\Vadym\workspace\library\jhu-0.6.jar Qualified Name:
 * org.jdamico.jhu.components.BinaryPartition Java Class Version: 6 (50.0)
 * JD-Core Version: 0.5.3
 */