@echo off

setlocal EnableDelayedExpansion

set JRR_JAVA_HOME=default jvm
SET JRR_JAVATYPE=2
SET JRR_JAVASTDFORCE=0
SET JRR_JAVAW=
SET JRR_FULL_CMD=
SET JRR_JAVA8_USED=1

for %%a in (%*) do (
  if "%%a"=="j11" (
    set JRR_JAVA_HOME=java11
    set DEFAULT_JVM11_OPTS=-Djdk.module.illegalAccess.silent=true --add-opens=java.base/java.lang=ALL-UNNAMED -Dsun.reflect.debugModuleAccessChecks=true --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/jdk.internal.reflect=ALL-UNNAMED --add-opens=java.base/jdk.internal.loader=ALL-UNNAMED --add-opens=java.base/jdk.internal.vm.annotation=ALL-UNNAMED --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED --add-reads=java.base=java.management --illegal-access=permit --add-modules=ALL-SYSTEM -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI -Djdk.attach.allowAttachSelf=true 
    set JRR_JAVA8_USED=0
   ) ELSE if "%%a"=="j15" (
    set JRR_JAVA_HOME=java15
    set JRR_JAVA8_USED=0
   ) ELSE if "%%a"=="jstd" (
       set JRR_JAVAW=
       set JRR_JAVATYPE=2
       set JRR_JAVASTDFORCE=1
   ) ELSE if "%%a"=="jw" (
     if "!JRR_JAVASTDFORCE!"=="0" (
       set JRR_JAVAW=w
       set JRR_JAVATYPE=3
     )
  )ELSE (
  set JRR_FULL_CMD=!JRR_FULL_CMD! %%a
  )
)

rem echo JRR_FULL_CMD = %JRR_FULL_CMD%
rem EXIT /B 1

if "!JRR_JAVA8_USED!"=="1" ( 
   set DEFAULT_JVM11_OPTS=!DEFAULT_JVM11_OPTS!
)

rem %JRR_JAVA_HOME%\bin\java%JRR_JAVAW% %DEFAULT_JVM11_OPTS%  %JAVA_OPTS% %GROOVY_OPTS% %JRR_GROOVY_OPTS2% %JRR_JAVATYPE% %JRR_FULL_CMD%

java %JAVA_OPTS% %*%

endlocal

if %ERRORLEVEL% == 0 goto:eof
echo bad exit code : %ERRORLEVEL%
pause
rem following exit from this and caller script
EXIT /B 1
:EXIT
