#!/bin/bash

echo "compiling..."
./gradlew shadowJar
./gradlew --stop
mv build/libs/Screenscraping*.jar ./SeleniumScraping.jar

echo "compiling finished"
