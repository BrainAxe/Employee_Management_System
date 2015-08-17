numbers = []

def NUMBER(b):
    i=0
    while i < b:
        print "At the top i is %d" % i
        numbers.append(i)
    
        i = i + 1
        print "Numbers now: ", numbers
        print "At the bottom i is %d" % i
    
a = int(raw_input("Number?"))
NUMBER(a)   
print "The numbers: "

for num in numbers:
    print num
