@echo off
set selection=%1
set menufile=menu.txt
if "%selection%"=="" call :choose
if "%selection%"=="" echo No program selected & goto end

set progname=
for /f "tokens=1,2 delims=," %%a in (%menufile%) do if "%%a"=="%selection%" set progname=%%b & goto next
:next
if "%progname%"=="" echo Program number %selection% not found in list & goto end
compile %progname%
goto end

:choose
echo Please choose one of the following programs by pressing the number then Return
echo.
type %menufile%
echo.
set /p selection=Please enter program number and Return: 
:end
