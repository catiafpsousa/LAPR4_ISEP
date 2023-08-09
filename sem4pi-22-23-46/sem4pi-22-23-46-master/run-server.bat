REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET BASE_CP=base.server.sharedboard\target\base.server.sharedboard-1.4.0-SNAPSHOT.jar;

REM call the java VM, e.g, 
java -cp %BASE_CP% SharedBoardServer
