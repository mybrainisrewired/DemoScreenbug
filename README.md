# DemoScreenbug
# Overview
Three decompiled APKS that together make a malware distribution system for android tablets.
cloudservices is a malware distribution system.
removing the malware cloudservice app resulted in DEMO being printed accross the screen via an edited system UI apk file.
a third program tries loading cloudservice libs; if they are not present it contacts the C&C to downaload a new one. 

# Meathod
the cloud service apk has access to su comandline and was downloading encrypted apk's and installing them using background shell commands.
 
# Removal
cloud services was removed and now large red letters saying DEMO appeared accross the screen at all times.
system ui was decompiled, the word DEMO was removed from the print to screen function. 
other references to call malware code were removed.
the system UI was recomiled and re-installed.


Taken from a chineese allwinner a13 tablet malware present in the orig stock firmware
the xml files where corrupted by the decompiler but 7zip extracting direct from the APK files can be used to fix these



