# sul.protocol.pocket132

import os

# change MCPE version
#REPLACE_FROM = "sul.protocol.pocket132"
#REPLACE_TO = "sul.protocol.bedrock137"
REPLACE_FROM = "Pocket132"
REPLACE_TO = "Bedrock137"

# update MCProtocolLib namespace
#REPLACE_FROM = "org.spacehq.mc"
#REPLACE_TO = "com.github.steveice10.mc"

#REPLACE_FROM = "cn.nukkit.utils.Binary"
#REPLACE_TO = "org.dragonet.proxy.utilities.Binary"

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
      if "import org.dragonet.proxy.protocol.packet.PEPacket" in l: continue;
      w.write(l.replace(REPLACE_FROM, REPLACE_TO))

if __name__ == "__main__":
  main();
