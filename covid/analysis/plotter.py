#!/usr/bin/env python

import sys
import csv
import matplotlib.pyplot as plt
from sklearn.preprocessing import MinMaxScaler
import numpy as np

save = True

l = [(line[0], line[1]) for line in csv.reader(sys.stdin)]
covid_arg, stock_arg = l[0]
l = l[1:]
x_values = [i for i in range(len(l))]
scaler = MinMaxScaler(feature_range=(0,10))

cases = scaler.fit_transform(np.array([int(i[0]) for i in l]).reshape(-1, 1))
change = np.array([float(i[1]) for i in l]).reshape(-1, 1)

plt.plot(x_values,change, 'b')
plt.plot(x_values,cases, 'g')


if stock_arg == "sp fluc":
    stock_val = "fluctuation"
elif stock_arg == "sp change":
    stock_val = "change"


plt.legend(("S&P 500 %s" % stock_val,"COVID %s (scaled to values between 0, 10" % covid_arg))
plt.plot(x_values,np.zeros(len(l)),'r--')
plt.title("S&P500 %s vs Total COVID %s in the US" % (covid_arg, stock_val))

plt.xlabel("Trading Days")
plt.ylabel("Percent change")

if save:
    plt.savefig(covid_arg + '-' + stock_val + '.png')
else:
    plt.show()









