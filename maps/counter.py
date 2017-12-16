from PIL import Image

im = Image.open('map_01.bmp')
print(im.mode)


count = 0
for x in range(0,im.size[0]):
	for y in range(x+1,im.size[1]):
		cur = im.getpixel((x,y))
		if cur == 113:
			count+= 1


print(count*2)