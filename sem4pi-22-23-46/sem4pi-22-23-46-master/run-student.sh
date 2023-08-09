#!/usr/bin/env bash

#REM set the class path,
#REM assumes the build was executed with maven copy-dependencies
export BASE_CP=base.app.student.console/target/base.app.student.console-1.4.0-SNAPSHOT.jar;

#REM call the java VM, e.g,
java -cp $BASE_CP eapli.base.app.student.console.StudentApp
