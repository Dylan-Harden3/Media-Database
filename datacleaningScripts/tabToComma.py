# takes names file formatted as
    # <nconst> <name>
# outputs names file as
    # <nconst>,<name>

# getting files for input
print("NOTE: put path of file from data directory")
nameInput = input("input file to read names from: ")
nameOutput = input("input file to write names to: ")

with open("../" + nameInput, "r") as fileRead:
    with open("../" + nameOutput, "w") as fileWrite:
        for line in fileRead.readlines():
            line = line.split("\t")
            for idx, item in enumerate(line):
                fileWrite.write(item)
                if (idx == 0): fileWrite.write(",")