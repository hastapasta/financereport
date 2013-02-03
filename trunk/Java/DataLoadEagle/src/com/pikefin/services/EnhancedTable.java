package com.pikefin.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import pikefin.log4jWrapper.Logs;

import com.pikefin.ApplicationSetting;
import com.pikefin.Constants;
import com.pikefin.PikefinUtil;
import com.pikefin.businessobjects.Column;
import com.pikefin.businessobjects.ExtractTable;
import com.pikefin.businessobjects.Job;
import com.pikefin.exceptions.CustomRegexException;
import com.pikefin.exceptions.PrematureEndOfDataException;
import com.pikefin.exceptions.TagNotFoundException;

public class EnhancedTable {
  private int nRowCount;
  private int nNumOfColumns;
  private String strTableSet;
  private String strSection;
  private DataGrabExecutor dg;
  private boolean bColTHTags;
  private String strEndDataMarker;
  private Job j;

  boolean bDoneProcessing;

  ExtractTable currentExtractTable;

  public EnhancedTable(DataGrabExecutor dg, String inputTableSet,
      String inputSection, Job inputJob) {
    this.dg = dg;
    this.strTableSet = inputTableSet;
    this.strSection = inputSection;
    this.bDoneProcessing = false;
    this.j = inputJob;

  }

  public ArrayList<String[]> enhancedGetTable() throws DataAccessException,
      TagNotFoundException, PrematureEndOfDataException {
    // retrieve the table data_set
    ArrayList<String[]> tabledata = new ArrayList<String[]>();
    if (Constants.EnhancedTableSection.ENHANCED_TABLE_BODY_SECTION
        .equals(strSection)) {
      currentExtractTable = j.getExtractKeyBody();
    }
    else if (Constants.EnhancedTableSection.ENHANCED_TABLE_COL_HEAD_SECTION
        .equals(strSection)) {
      currentExtractTable = j.getExtractKeyColhead();
    }
    else if (Constants.EnhancedTableSection.ENHANCED_TABLE_ROW_HEAD_SECTION
        .equals(strSection)) {
      currentExtractTable = j.getExtractKeyRowhead();
    }
    else {
      ApplicationSetting.getInstance().getStdoutwriter()
          .writeln("Table type not found. Exiting.", Logs.ERROR, "DG26.5");
      throw new TagNotFoundException();
    }

    Integer nFixedDataRows = currentExtractTable.getRowsOfData() != null 
        ? currentExtractTable
        .getRowsOfData() : 0;
    Integer nRowInterval = currentExtractTable.getRowInterval();
    strEndDataMarker = currentExtractTable.getEndDataMarker();
    this.bColTHTags = true;
    if (!Boolean.valueOf(true).equals(currentExtractTable.getColumnThTags()))
      this.bColTHTags = false;

    // seek to the top corner of the table

    String strTableCount = currentExtractTable.getTableCount();
    int nBeginTable, nEndTable;

    if (strTableCount.contains(",")) {
      nBeginTable = Integer.parseInt(strTableCount.substring(0,
          strTableCount.indexOf(",")));
      nEndTable = Integer.parseInt(strTableCount.substring(
          strTableCount.indexOf(",") + 1, strTableCount.length()));
    }
    else {
      nBeginTable = nEndTable = Integer.parseInt(strTableCount);
    }

    for (int nCurTable = nBeginTable; nCurTable <= nEndTable; nCurTable++) {

      int nCurOffset = 0;

      ApplicationSetting.getInstance().getStdoutwriter()
          .writeln("Searching table count: " + nCurTable, Logs.STATUS2, "DG27");
      nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<TABLE[^>]*>)", nCurTable,
          nCurOffset, dg.returned_content);

      nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<tr[^>]*>)",
          currentExtractTable.getTopCornerRow(), nCurOffset,
          dg.returned_content);

      int nEndRowOffset = PikefinUtil.regexSeekLoop("(?i)(</tr>)", 1,
          nCurOffset, dg.returned_content);

      int nEndTableOffset = PikefinUtil.regexSeekLoop("(?i)(</TABLE>)", 1,
          nCurOffset, dg.returned_content);

      // iterate over rows, iterate over columns
      boolean done = false;
      this.nNumOfColumns = currentExtractTable.getNumberOfColumns();

      String[] rowdata;
      this.nRowCount = 0;
      while (!done) {
        rowdata = enhancedProcessRow(dg.returned_content.substring(nCurOffset,
            nEndRowOffset));
        /*
         * This check is for when end_data_marker indicates the end of the
         * table.
         */
        if (this.bDoneProcessing == true)
          break;
        tabledata.add(rowdata);
        nRowCount++;
        /*
         * First part of if clause is for data sets with predefined fixed number
         * of rows.
         */
        if (nFixedDataRows > 0) {
          if (nFixedDataRows == nRowCount)
            break;
          try {
            nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<tr[^>]*>)",
                nRowInterval, nCurOffset, dg.returned_content);
            nEndRowOffset = PikefinUtil.regexSeekLoop("(?i)(</tr>)", 1,
                nCurOffset, dg.returned_content);
          }
          catch (TagNotFoundException tnfe) {
            throw new PrematureEndOfDataException();
          }

          if (nEndTableOffset < nCurOffset)
            throw new PrematureEndOfDataException();

        }
        /*
         * else part is for data sets of an indeterminate size.
         */
        else {
          try {
            nCurOffset = PikefinUtil.regexSeekLoop("(?i)(<tr[^>]*>)",
                nRowInterval, nCurOffset, dg.returned_content);
            nEndRowOffset = PikefinUtil.regexSeekLoop("(?i)(</tr>)", 1,
                nCurOffset, dg.returned_content);
          }
          catch (TagNotFoundException tnfe) {
            // No more TR tags in the file - we'll assume it's the
            // end of the table
            break;
          }

          // we're past the end table tag - done collecting table.
          if (nEndTableOffset < nCurOffset)
            break;
        }

      }

    }

    return (tabledata);
  }

  private String[] enhancedProcessRow(String strRow) 
      throws DataAccessException {

    String[] rowdata;
    ApplicationSetting.getInstance().getStdoutwriter()
        .writeln("row: " + nRowCount, Logs.STATUS2, "DG28");
    rowdata = new String[nNumOfColumns];
    int nRowOffset = 0;

    Set<Column> columns = (Set<Column>) currentExtractTable.getColumns();

    String strBeforeUniqueCodeRegex, strAfterUniqueCodeRegex, strDataValue;

    try {

      // for (int i = 0; i < nNumOfColumns; i++) {
      int i = 0;
      for (Column col : columns) {
        // while ()

        ApplicationSetting.getInstance().getStdoutwriter()
            .writeln("Column: " + col.getColCount(), Logs.STATUS2, "DG29");

        if (bColTHTags == false)
          /*
           * nRowOffset = UtilityFunctions.regexSeekLoop("(?i)(<td[^>]*>)",
           * rs.getInt("Column" + (i + 1)), nRowOffset,strRow);
           */
          nRowOffset = PikefinUtil.regexSeekLoop("(?i)(<td[^>]*>)",
              col.getColPosition(), nRowOffset, strRow);
        else
          nRowOffset = PikefinUtil.regexSeekLoop("(?i)(<th[^>]*>)",
              col.getColPosition(), nRowOffset, strRow);

        // String strBeforeUniqueCode = rs.getString("bef_code_col"
        // + (i+1));
        strBeforeUniqueCodeRegex = "(?i)(" + col.getBefCode() + ")";
        // String strAfterUniqueCode = rs.getString("aft_code_col" +
        // (i+1));
        strAfterUniqueCodeRegex = "(?i)(" + col.getAftCode() + ")";
        try {
          strDataValue = PikefinUtil.regexSnipValue(strBeforeUniqueCodeRegex,
              strAfterUniqueCodeRegex, nRowOffset, strRow);

          /* See below for exit conditions. */
          if (strEndDataMarker != null && (strEndDataMarker.length() > 0))
            if (strDataValue.equals(strEndDataMarker)) {
              this.bDoneProcessing = true;
              return null;
            }

          strDataValue = strDataValue.replace("\r", "");
          strDataValue = strDataValue.replace("\n", "");
          strDataValue = strDataValue.trim();
          /*
           * Going to strip out &nbsp; for all data streams, let's see if this
           * is a problem.
           */
          rowdata[i] = strDataValue.replace("&nbsp;", "");
          i++;
        }
        catch (CustomRegexException cre) {
          ApplicationSetting
              .getInstance()
              .getStdoutwriter()
              .writeln("Empty cell in table in url stream. Voiding cell.",
                  Logs.STATUS2, "DG30");
          rowdata[i] = "pikefinvoid";
        }
        catch (IllegalStateException ise) {
          ApplicationSetting
              .getInstance()
              .getStdoutwriter()
              .writeln(
                  "Invalid or Irregularly formed table row, " + 
                  "inserting pikefinvoid.",
                  Logs.WARN, "DG30.999");
          rowdata[0] = "pikefinvoid";
          /*
           * We have to return a value so that the rowheaders synch up with the
           * body
           */
          return (rowdata);
        }

      }

      return (rowdata);
    }
    catch (TagNotFoundException tnfe) {
      ApplicationSetting
          .getInstance()
          .getStdoutwriter()
          .writeln(
              "Invalid or Irregularly formed table row, inserting pikefinvoid.",
              Logs.WARN, "DG30.27");
      rowdata[0] = "pikefinvoid";
      return (rowdata);
    }
  }

}
