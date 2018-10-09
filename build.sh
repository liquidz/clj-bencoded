#!/bin/bash

export PATH=$PATH:/usr/local/graalvm/bin
lein uberjar
(cd target && native-image -jar bencoded.jar)
mkdir -p bin
\cp -pf target/bencoded bin

