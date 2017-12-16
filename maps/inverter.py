from PIL import Image

im = Image.open('map_01.bmp')
print(im.mode)


for x in range(0,im.size[0]):
	for y in range(x+1,im.size[1]):
		cur = im.getpixel((x,y))
		new = cur
		if cur == 252:
			new = 249
		im.putpixel((y,x),new)

im.save('map_01_mirrored.bmp')
im.show()
