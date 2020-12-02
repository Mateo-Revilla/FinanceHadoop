
#!/usr/bin/env python

import sys
import csv


d = {}
for index, line in enumerate(csv.reader(sys.stdin)):
    if index != 0:
        d[line[0]] = d.get(0,0) + int(line[2])

for key in d:
    print(key + ',' + str(d[key]))


