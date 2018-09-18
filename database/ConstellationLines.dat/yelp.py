from math import sin, cos, radians, sqrt

s = "id,hip,hd,hr,gl,bf,proper,ra,dec,dist,pmra,pmdec,rv,mag,absmag,spect,ci,x,y,z,Xn,Yn,Zn,rarad,decrad,pmrarad,pmdecrad,bayer,flam,con,comp,comp_primary,base,lum,var,var_min,var_max".split(',')


def f5():
	A = [-1] * 10000
	RES = []
	with open("StarDatabaseFinal.csv", "r") as f:
		f.readline()

		i = 0

		for line in f:
			data = line.split(',')

			if len(data[3]) > 0:
				A[int(data[3])] = i

			i += 1

	with open("Const.dat", "r") as f:
		f.readline()

		for line in f:

			data = line.split(",")

			for i in range(1, len(data)):
				l = int(data[i].split()[0])
				c = int(data[i].split()[1])

				if A[l] == -1 or A[c] == -1:
					break

				data[i] = str(A[l]) + " " + str(A[c])
			
			else:
				RES.append(data)
			

	with open("ConstFINAL.dat", "w") as f:
		f.write("\n")
		for i in RES:
			f.write(",".join(i)+"\n")	

def f4():
	A = [[]] * 10000

	RES = []
	
	with open("ConstellationLines.dat", "r") as f:
		f.readline()

		p = ""

		for line in f:
			data = line.split()

			ZR = [data[0]]

			last = int(data[2])

			for i in range(3, len(data)):

				cur = int(data[i])
				if cur == last:
					continue

				last = int(data[i-1])

				ZR.append(str(last)+" "+str(cur))


			if data[0] == p:
				for i in range(1, len(ZR)):
					RES[len(RES) - 1].append(ZR[i])

			else:
				RES.append(ZR)

			p = data[0]

	#print(RES)

	with open("Const.dat", "w") as f:
		f.write("\n")
		for i in RES:
			f.write(",".join(i)+"\n")

def f3():
	A = []
	with open("onlyNeed.csv", "r") as f:
		f.readline()
		
		for line in f:
			data = line.split(",")

			x = float(data[17])
			y = float(data[18])
			z = float(data[19])

			l = sqrt(x*x+y*y+z*z)

			data[20] = str(x/l);
			data[21] = str(y/l);
			data[22] = str(z/l);

			r = []
			for i in range(23):
				r.append(data[i])

			for i in range(27, 35):
				r.append(data[i])

			A.append(r)

	with open("StarDatabaseFinal.csv", "w") as f:
		f.write("\n")
		for i in A:
			f.write(",".join(i)+'\n')

def f2():
	MAX_MAG = 4

	A = [False] * 10000

	RES = []

	with open("ConstellationLines.dat", "r") as f:
		f.readline()

		for line in f:
			data = line.split()

			for i in range(1, len(data)):
				A[int(data[i])] = True


	with open("onlyHR.csv", "r") as f:
		RES.append(f.readline())

		for line in f:
			data = line.split(",")

			if float(data[13]) < MAX_MAG or A[int(data[3])]:
				RES.append(line)

	with open("onlyNeed.csv", "w") as f:
		for i in RES:
			f.write(i)

def f1():
	A = []

	with open("hygdata_v3.csv", "r") as f:
		f.readline()

		i = 1

		for line in f:
			data = line.split(",")

			i += 1

			if (len(data[3]) > 0):
				A.append(line)

	with open("onlyHR.csv", "w") as f:
		f.write("\n")
		for i in A:
			f.write(i)

f4()
f5()