from math import sin, cos, radians

A = []

fir = ""

with open("cities1.csv", "r") as f:
	fir = f.readline()

	for line in f:
		data = line.split(",")

		if float(data[4]) > 500000:
			A.append((data[1], data[2], data[3], data[5]))


A.sort(key = lambda x: x[0]);
	
with open("citiesFinal.csv", "w") as f:
	f.write(fir)
	for i in A:
		f.write(",".join(i) + "\n")