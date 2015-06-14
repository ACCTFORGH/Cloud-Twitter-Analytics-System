import sys
import happybase

s3files = []
with open("s3files.txt") as fr:
    for line in fr:
        s3files.append(line.split(' ')[-1].strip())

def delimited(file, delimiter='\2', bufsize=1024*1024*10):
    buf = ''
    while True:
        newbuf = file.read(bufsize)
        if not newbuf:
            return
        buf += newbuf
        lines = buf.split('\2')
        for line in lines[:-1]:
            yield line
        buf = lines[-1]

if __name__ == '__main__':
    
    if len(sys.argv) < 3:
        print 'invalid args: start end log'
        sys.exit(0)

    start = int(sys.argv[1])
    end = int(sys.argv[2])
    logfile = sys.argv[3]

    log = open(logfile, 'w')
    log.write('start: ' + str(start) + '\n')
    log.write('end: ' + str(end) + '\n')
    log.flush()

    url = '127.0.0.1'

    connection = happybase.Connection(url, autoconnect = False)
    connection.open()
    #print connection.tables()
    db = connection.table('q2')
    batch = db.batch()

    for i in range(start, end):
        f = s3files[i].split('/')[-1].split('.')[0] + '.output'
        log.write(f + '\n');
        log.flush()
        output = open(f, 'r')
        count = 0
        for line in delimited(output):
            terms = line.split('\1')
            if len(terms) != 5:
                continue
            tweet_id = terms[0]
            uid = terms[1]
            timestamp = terms[2]
            score = terms[3]
            text = terms[4]
            batch.put(uid + '+' + timestamp, {'tweet_profile:text' : text, 'tweet_profile:score' : score, 'tweet_profile:id' : tweet_id})
            count += 1
            if count == 500:
                batch.send()
                count = 0
        if count > 0:
            batch.send()
            count = 0

    connection.close()
    log.write("done\n")
    log.flush()
    log.close()

