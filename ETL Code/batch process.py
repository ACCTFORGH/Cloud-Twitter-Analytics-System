import subprocess
import os
import sys
from multiprocessing import Process

s3files = []
with open("s3files.txt") as fr:
	for line in fr:
		s3files.append(line.split(' ')[-1].strip())

def parse(f):
	print 'download: ' + f
	subprocess.call(['s3cmd', 'get', f])
	print 'uncompress...'
	gz = f.split('/')[-1]
	subprocess.call(['gunzip', gz])
	unziped = gz.split('.')[0]
	print 'parse...'
	subprocess.call(['java', '-cp', './gson-2.3.1.jar:.', 'JsonFilter', unziped, 'banned.txt', 'afinn.txt'])
	print 'clean up...'
	os.remove(unziped)
	return unziped + ".output"

if __name__ == '__main__':

	if len(sys.argv) < 3:
		print 'usage ' + sys.argv[0] + " start_index count"
		sys.exit(0)

	start = int(sys.argv[1])
	end = int(sys.argv[2]) + start
	numProcs = 4

	i = start
	while i < end:
		procs = []
		for j in range(i, i + numProcs):
			if j < len(s3files) and j < end:
				print 'processing ' + s3files[j]
				p = Process(target = parse, args=(s3files[j],))
				procs.append(p)
				p.start()
		for p in procs:
			p.join()
		i += numProcs
