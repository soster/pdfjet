#!/bin/bash

TEMP=$PATH

export JAVA_HOME=/opt/jdk1.5.0_22
PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH

rm docs/java/com/pdfjet/*.html
rm docs/_net/net/pdfjet/*.html
rm docs/java/*.html
rm docs/_net/*.html

cd src
javadoc -public -notree -noindex -nonavbar com/pdfjet/*.java -d ../docs/java
cd ..
javac util/Translate.java
java util.Translate

PATH=$TEMP
