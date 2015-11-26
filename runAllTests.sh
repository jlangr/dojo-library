#!/bin/bash
find . -name "*Test.java" \
    | sed -e "s/\.java//" \
          -e "s=./==" \
          -e "s=/=.=g" \
    | xargs java -classpath .:hamcrest-core-1.3.jar:junit-4.12.jar:mockito-all-1.8.4.jar org.junit.runner.JUnitCore
