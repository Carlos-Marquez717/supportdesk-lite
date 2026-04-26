@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup batch script for Windows
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set "BASEDIR=%~dp0"
set "PROJECTBASEDIR=%BASEDIR:~0,-1%"
set "WRAPPER_DIR=%BASEDIR%.mvn\wrapper"
set "WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar"
set "WRAPPER_PROPS=%WRAPPER_DIR%\maven-wrapper.properties"
set "WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

if "%MAVEN_USER_HOME%"=="" set "MAVEN_USER_HOME=%BASEDIR%.mvn\maven-user-home"

if not exist "%WRAPPER_JAR%" (
  echo Maven Wrapper jar not found. Downloading...
  if not exist "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $props = Get-Content '%WRAPPER_PROPS%' | Where-Object { $_ -match '^wrapperUrl=' } | Select-Object -First 1; $url = if ($props) { $props.Substring('wrapperUrl='.Length) } else { '%WRAPPER_URL%' }; [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri $url -OutFile '%WRAPPER_JAR%'"
  if errorlevel 1 (
    echo Failed to download Maven Wrapper jar.
    exit /b 1
  )
)

java -Dmaven.multiModuleProjectDirectory="%PROJECTBASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
endlocal
