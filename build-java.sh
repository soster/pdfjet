#!/bin/bash

cd src
javac -encoding utf-8 -Xlint com/pdfjet/*.java

jar cf PDFjet.jar com/pdfjet/*.class
mv PDFJet.jar ..
rm com/pdfjet/*.class
cd ..
cd examples

javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_01.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_02.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_03.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_04.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_05.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_07.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_08.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_13.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_14.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_15.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_16.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_17.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_18.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_19.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_21.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_22.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_23.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_24.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_25.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_26.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_30.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_32.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_33.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_34.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_36.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_37.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_38.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_41.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_42.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_43.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_45.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_46.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_47.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_48.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_50.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_51.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_52.java
javac -encoding utf-8 -Xlint -cp ../PDFjet.jar Example_53.java


java -cp ../PDFjet.jar:. Example_01
java -cp ../PDFjet.jar:. Example_02
java -cp ../PDFjet.jar:. Example_03
java -cp ../PDFjet.jar:. Example_04
java -cp ../PDFjet.jar:. Example_05
java -cp ../PDFjet.jar:. Example_07
java -cp ../PDFjet.jar:. Example_08
java -cp ../PDFjet.jar:. Example_13
java -cp ../PDFjet.jar:. Example_14
java -cp ../PDFjet.jar:. Example_15
java -cp ../PDFjet.jar:. Example_16
java -cp ../PDFjet.jar:. Example_17
java -cp ../PDFjet.jar:. Example_18
java -cp ../PDFjet.jar:. Example_19
java -cp ../PDFjet.jar:. Example_21
java -cp ../PDFjet.jar:. Example_22
java -cp ../PDFjet.jar:. Example_23
java -cp ../PDFjet.jar:. Example_24
java -cp ../PDFjet.jar:. Example_25
java -cp ../PDFjet.jar:. Example_26
java -cp ../PDFjet.jar:. Example_30
java -cp ../PDFjet.jar:. Example_32
java -cp ../PDFjet.jar:. Example_33
java -cp ../PDFjet.jar:. Example_34
java -cp ../PDFjet.jar:. Example_36
java -cp ../PDFjet.jar:. Example_37
java -cp ../PDFjet.jar:. Example_38
java -cp ../PDFjet.jar:. Example_41
java -cp ../PDFjet.jar:. Example_42
java -cp ../PDFjet.jar:. Example_43
java -cp ../PDFjet.jar:. Example_45
java -cp ../PDFjet.jar:. Example_46
java -cp ../PDFjet.jar:. Example_47
java -cp ../PDFjet.jar:. Example_48
java -cp ../PDFjet.jar:. Example_50
java -cp ../PDFjet.jar:. Example_51
java -cp ../PDFjet.jar:. Example_52
java -cp ../PDFjet.jar:. Example_53

