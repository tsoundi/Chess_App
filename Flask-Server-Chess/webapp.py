from flask import Flask, render_template
from flask import request
import threading
import json
from time import sleep

import discord
import re
import os
import keyboard
from werkzeug.utils import secure_filename
import asyncio
from PIL import Image

getting_FEN = 1
FENTEST=""

discord_loop = asyncio.new_event_loop()
intents = discord.Intents.default()
intents.message_content = True
client = discord.Client(loop=discord_loop, intents=intents)
app = Flask(__name__)

color = "cv.scan black"

@app.route('/', methods = ['POST'])
async def get_image():
    """<form action="classify_upload" method="post" id="upload-form">
    <input type="file" name="imagefile" id="imagefile"/>
    <input type="submit" />
    </form>
    """
    global color
    global FENTEST
    global getting_FEN
    print(request.form)
    print(request.files)
    print(request)
    print(request.files.get)

    try:
        if request.files.get('black') != None :
            imagefile = request.files.get('black')
            color = "cv.scan black"
        else:
            imagefile = request.files.get('white')
            color = "cv.scan white"
    except Exception as err:
        print(err)
    

    filename = secure_filename(imagefile.filename)
    imagefile.save(filename)

    import cv2, numpy as np
    img = cv2.imread(filename,0)

    blur = cv2.GaussianBlur(img,(5,5),0)
    _, img_binary = cv2.threshold(blur,0,255,cv2.THRESH_BINARY+cv2.THRESH_OTSU)
    cv2.imwrite(filename[:-4]+"_black.jpg",img_binary)
    
    image = Image.open(filename[:-4]+"_black.jpg")

    if (os.path.getsize(filename[:-4]+"_black.jpg")> 1000000
    ):
        print(os.path.getsize(filename[:-4]+"_black.jpg"))
        image.save(filename[:-4]+"_black.jpg", 
                 "JPEG", 
                 optimize = True, 
                 quality = 50)


    #pre_processing image
    #rechttrekken
    #zwart maken

    print(imagefile)
    #send imagefile to python bot
    print('2')
    sleep(1)
    print('1')
    sleep(1)
    #receive from python bot FEN code
    channel = client.get_channel()
    send_fut = asyncio.run_coroutine_threadsafe(channel.send(file=discord.File(filename[:-4]+"_black.jpg")), discord_loop)
    # wait for the coroutine to finish
    send_fut.result()
    
    while(getting_FEN):
        sleep(2)
        print("waiting")
    getting_FEN = 1
    print(FENTEST)
    
    result = {'FENCode': FENTEST}
    return json.dumps(result)


@client.event
async def on_ready():
    print(f'{client.user.name} has connected to Discord!')


@client.event
async def on_message(message):
    #print(message.embeds[0].fields[1].value)
    global getting_FEN
    global FENTEST
    print(message)
    if message.author == client.user:
        keyboard.write(color)
        keyboard.press("enter")

        return

    if message.content == "I could not find any chessboard in the image.":
        FENTEST = "error"
        getting_FEN = 0

    if message.content != "cv.scan black":
        for i, v in enumerate(message.embeds[0].fields):
            if "lichess" in v.value:
                URL = message.embeds[0].fields[i].value
                URL_split = re.split("/standard/", URL)
                FEN = "".join([str(string)for string in URL_split[1]])
                FEN = FEN.replace('_', ' ' )
                FEN = FEN[:-1]
                print(FEN)
                FENTEST = FEN
                getting_FEN = 0




def startClient():
    intents = discord.Intents.default()
    intents.message_content = True
    loop_client = asyncio.new_event_loop()
    client = discord.Client(intents=intents)

    
    asyncio.set_event_loop(loop_client)
    client.run('')

def start_loop(loop, gen):
    loop.run_until_complete(gen)

# Run the app
if __name__ == "__main__":
    
    
    discord_coroutine = client.start('')
    discord_thread = threading.Thread(target=start_loop, args=(discord_loop, discord_coroutine))
    discord_thread.start()

    app.run('192.168.1.9', 8080)
    

