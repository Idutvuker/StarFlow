from math import sin, cos, radians

A = []

with open("ConstellationPreData.csv", "r") as f:
	f.readline()

	for line in f:
		data = line.split(";")


		if (len(data[1]) > 0):
			ra = radians(float(data[2])*15)
			dec = radians(90.0-float(data[3]))

			x = sin(dec)*cos(ra)
			y = sin(dec)*sin(ra)
			z = cos(dec)

			A.append((data[0], data[1], str(x), str(y), str(z)))

		else:
			A.append((data[0], "", "", "", ""))

with open("ConstellationData.csv", "w") as f:
	f.write("CON;NAME;X;Y;Z\n")
	for i in A:
		f.write(";".join(i) + "\n")