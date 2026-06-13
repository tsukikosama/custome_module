#!/bin/sh
# Maven wrapper - delegates to user's Maven installation
MAVEN_HOME="D:/apache-maven-3.9.9"
exec "$MAVEN_HOME/bin/mvn" "$@"
