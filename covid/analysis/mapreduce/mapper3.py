#!/usr/bin/env python

import sys
import csv


d = {}
stock_arg = "sp fluc"
covid_arg = "cases"
print("%s,%s" % (covid_arg, stock_arg))
for index, line in enumerate(csv.reader(sys.stdin)):
    if index == 0:
        for i, item in enumerate(line):
            d[item] = i

    if index != 0:
        change_val = float(line[d[stock_arg]])
        covid_val = int(line[d[covid_arg]])
        print("%i,%f" % (covid_val,change_val))

