REM set the class path,
REM assumes the build was executed with maven copy-dependencies
SET BASE_CP=base.app.sharedboard\target\base.app.sharedboard-1.4.0-SNAPSHOT.jar;

REM call the java VM, e.g, 
java -cp %BASE_CP% eapli.base.app.sharedboard.console.SharedBoardApp
