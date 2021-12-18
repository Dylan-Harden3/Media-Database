# takes in file formated as 
    # (line number) (title ID) (nconst) (category) (job) (characters)
# outputs file (without header) with only actresses, actors, and directors formated as
    # (title ID) (nconst) (category)

# getting files for input and output
print("NOTE: put path of file from data directory")
fileInput = input("input file to read from: ")
fileOutput = input("input file to write to: ")

# deleting index 0, 4, 5 from principals
with open('../' + fileInput, 'r') as fileRead:
    with open('../' + fileOutput, 'w') as fileWrite:
        # fileWrite.write(fileRead.readline())  # header
        for lineIdx, lineRead in enumerate(fileRead.readlines()):
            lineRead = lineRead.split("\t")
            count = 0  # number of items written per row
            # only writing to new file if is actor, actress, director
            if (lineRead[3] != "actor" and 
                lineRead[3] != "actress" and 
                lineRead[3] != "director"):
                continue
            for idx, item in enumerate(lineRead):
                if (idx == 1 or idx == 2 or idx == 3):
                    fileWrite.write(item + "\t")
                    count += 1
            fileWrite.write("\n")
            if (count != 3): 
                print("line:", lineIdx, "-", count)