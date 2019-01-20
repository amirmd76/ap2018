#!/usr/bin/python3.6
import os
import subprocess
import sys

from PIL import Image


def get_file_name(s):
    sr = s[::-1]
    if '/' in s:
        ls = sr[:sr.find('/')]
    else:
        ls = sr
    return ls[ls.find('.')+1:][::-1]


loc = sys.argv[1]
dir = os.path.dirname(loc)
folder = os.path.join(dir, get_file_name(loc))
subprocess.call(["rm", "-rf", folder])
subprocess.call(["mkdir", "-p", folder])
y = int(sys.argv[2])
x = int(sys.argv[3])
ignore = 0
if len(sys.argv) > 4:
    ignore = int(sys.argv[4])
img = Image.open(loc)
w = img.width
h = img.height
uw = w/x
uh = h/y

for i in range(x):
    for j in range(y):
        idx = x * j + i
        if (ignore > 0 and x * y - idx <= ignore) or (ignore < 0 and idx < -ignore):
            continue
        cp = img.crop((i * uw, j * uh, i * uw + uw, j * uh + uh))
        fname = os.path.join(folder, "{}.png".format(idx))
        subprocess.call(["touch", fname])
        cp.save(fname)

fname = os.path.join(folder, "info.txt")
subprocess.call(["touch", fname])
with open(fname, "w+") as f:
    f.write("{}\n".format(x * y - ignore))
