#!/bin/bash
cd src
javac */*.java
jar cvfm ../Stoichiometry.jar manifest.txt */*.class
cd ..
