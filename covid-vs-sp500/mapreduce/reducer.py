#!/usr/bin/env python

import sys
import csv

# extracting cases and change columns 
lines = [i for i in csv.reader(sys.stdin)]
for index, line in enumerate(lines):
    change_val = float(line[1])
    covid_growth = 1
    if index > 1:
        covid_growth = int(lines[index-1][0]) - int(line[0])
    print('%i,%.2f' % (covid_growth,change_val))