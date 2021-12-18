# takes in names file formatted as
    # <nconst>,<name>
# takes in principals file formatted as 
    # <title id> <nconst> <category>
# outputs new names and principals file with only the people that occur in both files

# getting files for input
print("NOTE: put path of file from data directory")
nameInput = input("input file to read names from: ")
nameOutput = input("input file to write names to: ")
principalsInput = input("input file to read principals from: ")
principalsOutput = input("input file to output principals to: ")

names = {}  # dictionary to store nconst and name from names input file for comparison against principals

# reading in names 
with open("../" + nameInput, 'r') as nameRead: 
    for lineRead in nameRead.readlines():
        lineRead = lineRead.split(",")
        # storing line in names dict
        names[lineRead[0]] = lineRead[1]

# reading in principals, if principal name is in names >> can output to both new names and principals files
with open("../" + principalsInput, 'r') as principalsRead:
    with open("../" + nameOutput, 'w') as nameWrite:
        with open("../" + principalsOutput, 'w') as principalsWrite:
            # need to read through lines in principals >> test for existance in names
            for lineRead in principalsRead.readlines():
                lineRead = lineRead.split("\t")
                if (lineRead[1] in names):
                    # writing to names if hasn't already been written
                    if (names[lineRead[1]]):  # value will be false if its already been written to
                        nameWrite.write(lineRead[1] + "," + names[lineRead[1]])
                        # changing value at key for names since it has already been written to names file
                        names[lineRead[1]] = False
                    # writing to principals
                    principalsWrite.write("\t".join(lineRead))