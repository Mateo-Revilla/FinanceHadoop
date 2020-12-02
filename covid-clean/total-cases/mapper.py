
#!/usr/bin/env python

import sys
import csv

# key: date | value : count

d = {}
for index, line in enumerate(csv.reader(sys.stdin)):
    if index != 0:
        try:
            d[line[0]] += int(line[2])
        except KeyError:
            d[line[0]] = int(line[2])

for key in d:
    print(key + ',' + str(d[key]))



