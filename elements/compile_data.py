#!/usr/bin/python
import os, json

def jsonFormat(o):
  return json.dumps(o, sort_keys=True, indent=4, separators=(',', ': '))


dataDir = "data"

files = os.listdir(dataDir)

#print files

elements = {}
#structure {'H': {'propertyName':'value'}}
for filename in files:
#  print filename
  filepath = os.path.join(dataDir, filename)
  with open(filepath, 'r') as f:
    lines = f.read().splitlines()
    i = 1
    numfields = int(lines[0])
    symbolIndex = -1
    titles = []
    for j in range(i, i + numfields):
      titles.append(lines[j])
      if lines[j] == "symbol":
        symbolIndex = j-i
    if symbolIndex == -1:
      print "no 'symbol' column"
      print "  file: " + str(filepath)
      print "  titles: " + str(titles)
      print "  numfields: " + str(numfields)
      quit()
    i += numfields
    while i+numfields < len(lines):
      fields = []
      for j in range(i, i + numfields):
        fields.append(lines[j])
        if j-i == symbolIndex:
          symbol = lines[j]
#          print symbol
      for j in range(numfields):
        if j != symbolIndex:
          value = fields[j]
          if not value == "":
            try:
              value = int(value)
            except ValueError:
              try:
                value = float(value.replace(",", "."))
              except ValueError:
                pass
            if not symbol in elements:
              elements[symbol] = {}
            if titles[j] in elements[symbol]:
              prevValue = elements[symbol][titles[j]]
              if value != prevValue:
                print 'conflicting data! (new value not equal to previous)'
                print "  file: " + str(filepath)
                print '  symbol: ' + str(symbol)
                print '  title: ' + str(titles[j])
                print '  prevValue: ' + str(prevValue)
                print '  value: ' + str(value)
                quit()
            elements[symbol][titles[j]] = value
      i += numfields
#print jsonFormat(elements)
print jsonFormat(elements['K'])
print jsonFormat(elements['Kr'])



