@echo off

javaw %JAVA_OPTS% %*%
if %ERRORLEVEL% == 0 goto:eof
echo bad exit code : %ERRORLEVEL%
pause
EXIT /B 1
:EXIT
