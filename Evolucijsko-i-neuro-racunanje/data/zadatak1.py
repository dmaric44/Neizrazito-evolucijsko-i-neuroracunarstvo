import matplotlib.pyplot as plt
import numpy as np

x = np.linspace(-8,10,1000)
color = ['blue', 'orange', 'magenta']
s = [1,0.25,4]

for i in range(len(s)):
    y = 1 / (1 + abs((x-2)/s[i]))
    plt.plot(x,y, color=color[i], label='s='+str(s[i]))

plt.legend(loc='best')
plt.show()

