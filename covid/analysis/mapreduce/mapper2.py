#!/usr/bin/env python

import sys
import csv


d = {
    "cases" : 7,
    "sp fluc" : 5,
    "sp change" : 6,
    "deaths" : 8
}
stock_arg = "sp change"
covid_arg = "deaths"

for index, line in enumerate(csv.reader(sys.stdin)):
    change_val = float(line[d[stock_arg]])
    covid_val = int(line[d[covid_arg]])
    print("%i,%f" % (covid_val,change_val))

