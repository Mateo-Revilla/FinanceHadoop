#!/usr/bin/env python

import sys
import csv


count = 0

for index, line in enumerate(csv.reader(sys.stdin)):
    count += int(line[1])

print("Total : %i " % count)



