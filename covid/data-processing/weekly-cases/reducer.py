#!/usr/bin/env python

import sys
import csv


weeks = []
week = {"count" : 0}

for index, line in enumerate(csv.reader(sys.stdin)):
    week["count"] += int(line[1])
    if index % 7 == 0:
        week["start"] = line[0]
    if index % 7 == 6:
        week["end"] = line[0]
        weeks.append(week)
        week = {"count" : 0}

if week.get("start"):
    weeks.append(week)

for week in weeks:
    print(week["start"] + '-' + week.get("end","          ") + " " + str(week["count"]))



