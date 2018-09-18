from math import sin, cos, radians

A = []

with open("data.txt", "r") as f:
	for line in f:
		k = line.find(" ")
		print(line[:k] +","+line[k+1:-1])