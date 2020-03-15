# TwitchStreamerReactionClipperV2
 Program that takes screenshots of kills on the game of Apex Legends, extracts the name, runs the name through a database of usernames, and clips the streamer on Twitch.tv if they are live. I send the screenshot to an image-to-text API to extract the name and I use my own Twitch API wrapper to clip the reaction in real time.

## Prerequisites
1. Java API for JSON Processing json-simple
2. Twitch API Dashboard Information ie. Client-ID, Client-Secret, Callback URI
3. Free OCR API key - image to text
4. Path to serialized map of key: origin username, value: twitch username
 -created using the program https://github.com/camachc2/StreamerCollector
5. Download the AutoHotKey scripting language to run .ahk files in the background
6. Create a jar file for this program. The path to this jar file is needed in the .ahk file
7. OpenCV image processing library to filter images without text.

## How does it do it?

1. Run the afk file "run.ahk" waiting for a special key conbination in the background (CTRL j in my case). File toggles on/off automatic screenshots every 2.1 seconds that get processed by the java program in the background. (2.15 seconds being the amount of time it takes for the kill text to disappear)
2. Play apex legends and toggle automatic screenshots on whenever you're battling outher players.
3. The java program will automatically try to determine if you get a kill, knock down, or assist. 
4. OpenCV image processing library crops the image to a 200x40 pixel section of the image that has the text indicating the kill. 
5. Then taking the laplace and calculating the statistical variance will determine if there's text in that small section of the image.
6. If the program deterimes there is text, send the image to the Free OCR image-to-text API and get the text in the image.
7. Then it will extract the name from the image, run it againts a map of origin usernames in "usernameStreamerMap.ser".
8. If the origin username is in the map attempt to clip the streamer.
9. If they are live it creates a folder with the link to the clip editor on twitch.
10. The java program ends and "run.ahk" continues to continually takes screenshots until toggled off.

## Why did I do this?
If you have played the game Apex Legends you have no doubt ran into twitch streamers in your lobbies. They tend to be really good at the game so I wanted their reaction if I ever got to beat them. Here is my favorite reaction from the #1 kills with wraith player in the world at the time https://clips.twitch.tv/ToughSaltyCaribouPhilosoraptor. His reaction is the reason I created this program. Update: I got him again! https://clips.twitch.tv/HelpfulJollyToothStinkyCheese my origin name is IdkhowtoplayApex

## What changed from V1 to V2?
1. Autohotkey File: V1 would require for the user to screenshot each kill manually by pressing a button. V2 has a toggle that sends the screenshot automatically every 2.1 seconds.
2. Filtering: V1 would filter the amount of images by you having to maually think about each screenshot you take. V2 filters the images via image processing to determine if there where the text "ELIMINATION player123" appears on your screen.
3. Cleaner Code: V2 driver file has smaller more descriptive functions as well as better exception handling. 

## Why filter the amount of images that go through the process?
I wanted to balance the amount of work sampling images for usernames vs automation. A toggle offers a good balance determining when I believe I need to start sampling. Additonally, the free ORC API has a limit of 500 images per day, so filtering allows me to stay under the cap. Since the run.ahk takes screenshots every 2.1 seconds, it would take around 18 minutes before I ran out of free OCR processing. OpenCV filters out the grand majority of the useless images created every 2.1 seconds allowing the 500 API calls to last all day.

## How does the program determine if you get a kill?
Taking the laplace of an image essentially highlights hard transitions in color in a greyscaled image. The idea is to crop the screenshot only to the part of the image that has the text "ELIMINATION player123" and take the varience. Since a zoomed in image of text would create a lot of hard edges, taking the varience of the laplace is a very good indicator of when you got a kill. When the image has no elimination text the program would take the laplace of a zoomed in picture of the ground usually equating to low varience. 
