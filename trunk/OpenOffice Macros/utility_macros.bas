Sub Main
'Macro1
add_basicide_menu
End Sub

Sub Macro1
dim oSheet as object
dim oChart as object
dim oData as object
dim tmpArray as object
dim test as String

SheetCount = ThisComponent.Sheets.getCount
oSheet=ThisComponent.Sheets(4)
oChart = oSheet.Charts(0).EmbeddedObject
'MsgBox oChart.dbg_methods
oChart.lockControllers
oChart.Title.String = "NEWER TITLE"
oChart.unlockControllers
BasicLibraries.loadLibrary("XrayTool")
'xray oChart
oData = oChart.getData()
xray oData
'test = oData.getRowDescriptions()
'MsgBox test
tmpArray = oData.getData()
if (IsNull(tmpArray)) then 
	xray tmpArray(0)
else
 MsgBox ("object null")
end if 

'xray tmpArray(0)



rem document   = ThisComponent.CurrentController.Frame
rem dispatcher = createUnoService("com.sun.star.frame.DispatchHelper")


End Sub

Sub Macro2

End Sub





Sub add_basicide_menu 
  sToolbar = "private:resource/menubar/menubar" 
  'Dim oImageManager As Object 
  oModuleCfgMgrSupplier = createUnoService("com.sun.star.ui.ModuleUIConfigurationManagerSupplier") 
  oModuleCfgMgr = oModuleCfgMgrSupplier.getUIConfigurationManager("com.sun.star.sheet.SpreadsheetDocument" ) 
  
  oToolbarSetting = oModuleCfgMgr.getSettings(sToolbar,true) 

  sMenu1 = ".uno:BasicIDEAppear" 
  sMenu1Label = "BasicIDE" 
  aMenu1 = CreateMenuItem( sMenu1, sMenu1Label ) 

  For i = 0 To oToolbarSetting.getCount() -1 
    If GetProperty("CommandURL", oToolbarSetting.getByIndex(i)) = ".uno:HelpMenu" Then 
      oMenu = GetProperty( "ItemDescriptorContainer", oToolbarSetting.getByIndex(i) ) 
    End If 
  Next i 

  oMenu.insertByIndex( oMenu.getCount(), aMenu1 ) 

  oModuleCfgMgr.replaceSettings(sToolbar, oToolbarSetting) 
  oModuleCfgMgr.store() 
End Sub 

Function CreateMenuItem( sCommand As String, sLabel As String ) as Variant 
  Dim aMenuMember(3) As New com.sun.star.beans.PropertyValue 

  aMenuMember(0).Name = "CommandURL" 
  aMenuMember(0).Value = sCommand 
  aMenuMember(1).Name = "HelpURL" 
  aMenuMember(1).Value = "" 'sHelpURL 
  aMenuMember(2).Name = "Label" 
  aMenuMember(2).Value = sLabel 
  aMenuMember(3).Name = "Type" 
  aMenuMember(3).Value = 0 
  
  CreateMenuItem = aMenuMember() 
End Function 

Function GetProperty( sName As String, aValues ) 
  For i = 0 To UBound(aValues()) 
    If aValues(i).Name = sName Then 
      GetProperty = aValues(i).Value 
    End If 
  Next i 
End Function 






