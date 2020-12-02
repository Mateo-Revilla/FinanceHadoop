#!/usr/bin/env python

import sys
import csv

# f = open('covid-data-clean.csv', 'w'

'''
Turns clean covid data to daily cases
'''

cases = {}
deaths = {}

for line in csv.reader(sys.stdin):

    try:
        cases[line[0]] += int(line[2])
        deaths[line[0]] += int(line[3])
    except KeyError:
        cases[line[0]] = int(line[2])
        deaths[line[0]] = int(line[3])


lines = []
for key in cases:
    line = [key] + [cases[key]] + [deaths[key]]
    lines.append(line)

for i, line in enumerate(lines):
    # if i == 0:
    #     line = [str(i) for i in line] + [str(line[1]),str(line[2])]
    # else:
    #     new_cases = line[1] - lines[i-1][1]
    #     death_cases = line[2] - lines[i-1][2]
    #     line = [str(i) for i in line] + [str(new_cases)] + [str(death_cases)]
    line = [str(i) for i in line]

    print(','.join(line))







