# sul.protocol.pocket132

import os

REPLACE_FROM = "sul.protocol.pocket132"
REPLACE_TO = "sul.protocol.pocket113"

def main():
  g = os.walk("./proxy")
  for path,d,filelist in g:
    for filename in filelist:  
      if filename.endswith(".java"):
        p = os.path.join(path, filename)
        print(p)
        performReplace(p)


def performReplace(file_path):
  with open(file_path, "r") as r:
    lines = r.readlines()
  with open(file_path, "w") as w:
    for l in lines:
      w.write(l.replace(REPLACE_FROM, REPLACE_TO))

if __name__ == "__main__":
  main();
