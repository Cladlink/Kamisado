@echo off
cd base_traductions
dir /B "*.properties" >sauv.txt

for /f %%i IN (sauv.txt) DO (
	native2ascii -encoding UTF-8 %%i ..\%%i
)

del sauv.txt