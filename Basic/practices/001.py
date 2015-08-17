from sys import argv

script, sf = argv

a = raw_input("Enter username\n > ")
b = raw_input("Enter password\n > ")

sfile = open(sf)
e = sfile.seek(0,6)
print e
sfile.close()

