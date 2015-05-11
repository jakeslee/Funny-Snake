#!/bin/bash

if [[ "$1" != "Version.java" ]];then
    echo $1;
    exit 0;
fi
echo $2
VER=`git rev-list HEAD | wc -l | awk '{print $1}'`
SHA1=`git rev-list HEAD -n 1 | cut -c 1-6`

MAXVER=$(($VER/100))
SECVER=$((($VER-100*$MAXVER)/10))
MINVER=$(($VER-100*$MAXVER-10*$SECVER))

sed -i '' "s/shortversion=\".*\"/shortversion=\"$MAXVER.$SECVER.$MINVER ($SHA1)\"/g" "Ant/build.properties"
sed -i '' "s/final String Version.*/final String Version = \"$MAXVER.$SECVER.$MINVER ($SHA1)\";/g" "$2"

sleep 0.5

