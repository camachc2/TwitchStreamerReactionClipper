#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
; #Warn  ; Enable warnings to assist with detecting common errors.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.


^j::
Toggle:=!Toggle
	If (Toggle){
		SoundPlay, StreamerLookup\Sounds\on.wav
		SetTimer, RunStreamerLookup, -1
	}
	Else{
		SoundPlay, StreamerLookup\Sounds\off.wav
	}
return

RunStreamerLookup:
While (Toggle){
    Send, {Alt Down}{PrintScreen}{Alt Up}
    Run, javaw -jar StreamerLookup\dist\StreamerLookup.jar
    Sleep, 2100
}
Return