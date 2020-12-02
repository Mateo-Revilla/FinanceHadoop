#!/usr/bin/env python

import sys
import csv

for index, line in enumerate(csv.reader(sys.stdin)):
    if index != 0:
        print(1)


