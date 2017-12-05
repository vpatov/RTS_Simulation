from PIL import Image

im = Image.open('map_01.bmp')
for x in range(0,im.size[0]):
    for y in range(x+1,im.size[1]):
        im.putpixel((y,x),im.getpixel((x,y)))

im.save('map_01_mirrored.bmp')
im.show()
