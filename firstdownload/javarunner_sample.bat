@echo off

java %JAVA_OPTS% %*%
if %ERRORLEVEL% == 0 goto:eof
echo bad exit code : %ERRORLEVEL%
pause
rem following exit from this and caller script
EXIT /B 1
:EXIT
