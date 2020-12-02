#!/usr/bin/env python

import sys
import csv

for index, line in enumerate(csv.reader(sys.stdin)):
    if index != 0:
        del line[3]
        del line[1]
        print(','.join(line))
