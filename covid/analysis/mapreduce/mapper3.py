#!/usr/bin/env python

import sys
import csv

# extracting cases and change columns 
d = {}
sp_arg = "sp change"
covid_arg = "cases"
for index, line in enumerate(csv.reader(sys.stdin)):
    if index == 0:
        for i, item in enumerate(line):
            d[item] = i

    if index != 0:
        change_val = float(line[d[sp_arg]])
        covid_val = int(line[d[covid_arg]])
        print('%i,%f' % (covid_val,change_val))
