FROM ibmjava:8-sdk

RUN apt update && apt install python2 python-pip unzip -y

RUN python2 -m pip install bitstring

RUN wget -P /usr/src https://github.com/andrewleech/jxe2jar/archive/refs/heads/master.zip

WORKDIR /usr/src

RUN unzip master.zip &&  mv jxe2jar-master jxe2jar && rm -rf master.zip

WORKDIR /usr/src/jxe2jar/src
