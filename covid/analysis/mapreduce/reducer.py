#!/usr/bin/env python

import sys
import csv

# extracting cases and change columns 
lines = [i for i in csv.reader(sys.stdin)]
for index, line in enumerate(lines):
    if index == 0:
        print(line[0] + ',' + line[1])
        continue
    change_val = float(line[1])
    print('%i,%f' % (int(line[0]),change_val * 100))
