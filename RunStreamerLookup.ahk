﻿#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
; #Warn  ; Enable warnings to assist with detecting common errors.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.

^j::
Send, {Alt Down}{PrintScreen}{Alt Up}
Run, javaw -jar C:\Users\cesar\Documents\NetBeansProjects\StreamerLookup\dist\StreamerLookup.jar 
return