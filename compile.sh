#!/bin/bash

echo "compiling..."
./gradlew shadowJar
./gradlew --stop
mv build/libs/SeleniumScraping*.jar ./SeleniumScraping.jar

echo "compiling finished"
