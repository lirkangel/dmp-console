#!/bin/bash

while getopts ":i:o:m:" opt; do
  case $opt in
    i) FILENAME="$OPTARG"
    ;;
    o) OUTPUT="$OPTARG"
    ;;
    m) HEAP_RAM="$OPTARG"
	;;
    \?) echo "Invalid option -$OPTARG" >&2
    ;;
  esac
done
if [ -z "$FILENAME" ] ;
then
    FILENAME="input.txt"
fi
if [ -z "$OUTPUT" ]; then
   OUTPUT="output.txt"
fi
if [ -z "$HEAP_RAM"  ]; then
    HEAP_RAM="256m"
fi
if [  -z "$TEMP_FILES_DIR" ]; then
    TEMP_FILES_DIR="/tmp/externalsorting"
fi
jarfile="dmp-console.jar"
if [ ! -e external-sorting.jar ]; then
    jarfile="target/dmp-console.jar"
fi

java -server -Xmx$HEAP_RAM -XX:+TieredCompilation -Dfilename=$FILENAME -Doutput=$OUTPUT -DtempFilesDir=$TEMP_FILES_DIR -jar $jarfile $1
