#!/usr/bin/env python

import sys
import csv

f = open('covid-data-clean.csv', 'w')

cases = {}
deaths = {}

for line in csv.reader(sys.stdin):

    try:
        cases[line[0] + ',' + line[1]] += int(line[2])
        deaths[line[0] + ',' + line[1]] += int(line[3])
    except KeyError:
        cases[line[0] + ',' + line[1]] = int(line[2])
        deaths[line[0] + ',' + line[1]] = int(line[3])

for key in cases:
    line = key + ',' + str(cases[key]) + ',' + str(deaths[key])
    print(line)


f.close()




