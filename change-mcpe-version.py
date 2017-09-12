# sul.protocol.pocket132

import os


def main():
  g = os.walk("./proxy")
  for path,d,filelist in g:
    for filename in filelist:  
      if filename.endswith(".java"):
        print(os.path.join(path, filename))


if __name__ == "__main__":
  main();
