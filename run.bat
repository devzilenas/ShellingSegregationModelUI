@echo off
set CLASS_DIR=classes
if not exist %CLASS_DIR% mkdir %CLASS_DIR%
@echo on
javac -d %CLASS_DIR% -sourcepath .;src\Grid\ Application.java
java -classpath %CLASS_DIR% Application
