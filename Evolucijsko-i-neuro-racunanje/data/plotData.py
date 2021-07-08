import matplotlib.pyplot as plt
from matplotlib.text import TextPath
import sys

file = open("zad7-dataset.txt")
paramsFile = open(sys.argv[1])
x1 = []
y1 = []
x2 = []
y2 = []
x3 = []
y3 = []

for line in file:
    sample = [float(x) for x in line.strip().split()]

    if(sample[2]==1.0):
        x1.append(sample[0])
        y1.append(sample[1])
    elif(sample[3]==1.0):
        x2.append(sample[0])
        y2.append(sample[1])
    else:
        x3.append(sample[0])
        y3.append(sample[1])

arch = [int(x) for x in paramsFile.readline().rstrip().split('x')]
type1 = arch[1]

number = [str(i+1) for i in range(type1)]
for i in range(type1):
    params = [float(x) for x in paramsFile.readline().rstrip().split()]
    plt.scatter(params[0], params[2], marker=TextPath((0, 0), number[i], size=1000),s=150, color='black')

        
plt.scatter(x1,y1, marker='.', color='red', label='100')
plt.scatter(x2,y2, marker='x', color='blue', label='010')
plt.scatter(x3,y3, marker='D', color='magenta', label='001')

plt.legend(loc='best')
plt.show()
