#!/usr/bin/env python

import sys
import csv
import matplotlib.pyplot as plt
from sklearn.preprocessing import MinMaxScaler
import numpy as np

save = True

def get_labels(number):
    if number == 1:
        return "cases", " %% growth"
    if number == 2:
        return "deaths", " %% growth"
    if number == 3:
        return "cases", " %% fluctuation"
    if number == 4:
        return "deaths", " %% fluctuation"


if len(sys.argv) == 2:
    covid_val, stock_val = get_labels(int(sys.argv[1]))
else:
    raise ValueError("Pass in number 1-4 as argument that matches the job number. e.g 'cat job1 | python plotter.py 1'.")

data = sorted([(int(line[0]), float(line[1])) for line in csv.reader(sys.stdin)], key=lambda x: x[0])


x_values = [i for i in range(len(data))]
scaler = MinMaxScaler(feature_range=(0,10))

cases = scaler.fit_transform(np.array([i[0] for i in data]).reshape(-1, 1))
change = np.array([i[1] for i in data]).reshape(-1, 1)

plt.plot(x_values,change, 'b')
plt.plot(x_values,cases, 'g')


plt.legend(("S&P 500 %s" % stock_val,"COVID %s (scaled to values between 0, 10" % covid_val))
plt.plot(x_values,np.zeros(len(data)),'r--')
plt.title("S&P500 %s vs Total COVID %s in the US" % (stock_val, covid_val))

plt.xlabel("Trading Days")
plt.ylabel("Percent change")

if save:
    plt.savefig('results/' + 'chart' + sys.argv[1] + '.png')
else:
    plt.show()




    





