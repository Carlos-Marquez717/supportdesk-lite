#!/bin/sh

BASEDIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_DIR="$BASEDIR/.mvn/wrapper"
WRAPPER_JAR="$WRAPPER_DIR/maven-wrapper.jar"
WRAPPER_PROPS="$WRAPPER_DIR/maven-wrapper.properties"
WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

if [ -z "$MAVEN_USER_HOME" ]; then
  MAVEN_USER_HOME="$BASEDIR/.mvn/maven-user-home"
  export MAVEN_USER_HOME
fi

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Maven Wrapper jar not found. Downloading..."
  mkdir -p "$WRAPPER_DIR"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR"
  else
    echo "Neither curl nor wget is available to download Maven Wrapper jar." >&2
    exit 1
  fi
fi

exec java -Dmaven.multiModuleProjectDirectory="$BASEDIR" -classpath "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"
