# TwitchStreamerReactionClipper
 Program that takes screenshots of kills on the game of Apex Legends, extracts the name, runs the name through a database of usernames, and clips the streamer on Twitch.tv if they are live. I send the screenshot to an image-to-text API to extract the name and I use my own Twitch API wrapper to clip the reaction in real time.

## Prerequisites
1. Java API for JSON Processing json-simple
2. Twitch API Dashboard Information ie. Client-ID, Client-Secret, Callback URI
3. Free OCR API key - image to text
4. Path to serialized map of key: origin username, value: twitch username
 -created using the program https://github.com/camachc2/StreamerCollector
5. Download the AutoHotKey scripting language to run .ahk files in the background
6. Create a jar file for this program. The path to this jar file is needed in the .ahk file

## How does it do it?

1. Have the AutoHotKey file "RunStreamerLookup.ahk" running waiting for a special key conbination in your keyboard  in the background (crtl j in my case)
2. Play apex legends and get either a kill, knock down, assist, or crate access.
3. Press the key conbination.
4. "RunStreamerLookup.ahk" will printScreen and run the jar version of the java program.
5. The java program will then take the screenshot from the clipboard.
6. Send the file to the Free OCR image-to-text API and get the text in the image.
7. Then it will extract the name from the image, run it againts a map of origin usernames.
8. if the origin username is in the map attempt to clip the streamer.
9. if they are live it creates a folder with the link to the clip editor on twitch.
10. Program ends and "RunStreamerLookup.ahk" continues to wait for the key combination to start the process again all running in the background.

## Why did I do this?
If you have played the game Apex Legends you have no doubt ran into twitch streamers in your lobbies. They tend to be really good at the game so I wanted their reaction if I ever got to beat them. Here is my favorite reaction from the #1 kills with wraith player in the world at the time https://clips.twitch.tv/ToughSaltyCaribouPhilosoraptor. His reaction is the reason I created this program.
