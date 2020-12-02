#!/usr/bin/env python

import sys
import csv
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler, MinMaxScaler
import numpy as np

count = 0

l = [(line[0], line[1]) for line in csv.reader(sys.stdin)]
x_values = [i for i in range(len(l))]
scaler = MinMaxScaler(feature_range=(0,10))

cases = scaler.fit_transform(np.array([int(i[0]) for i in l]).reshape(-1, 1))
change = np.array([float(i[1]) for i in l]).reshape(-1, 1)

plt.plot(x_values,change, 'b')
plt.plot(x_values,cases, 'g')
plt.legend(("S&P 500","COVID (scaled to values between 0, 10"))
plt.plot(x_values,np.zeros(len(l)),'r--')
plt.title("S&P500 vs Total COVID in the US")
plt.xlabel("Trading Days")
plt.ylabel("Percent change")

plt.show()









