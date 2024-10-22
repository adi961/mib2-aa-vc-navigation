#!/bin/sh
export PATH=/proc/boot:/bin:/usr/bin:/usr/sbin:/sbin:/mnt/app/media/gracenote/bin:/mnt/app/armle/bin:/mnt/app/armle/sbin:/mnt/app/armle/usr/bin:/mnt/app/armle/usr/sbin:$PATH

if [ "$_" = "/bin/on" ]; then BASE="$0"; else BASE="$_"; fi
SCRIPTDIR=$( cd -P -- "$(dirname -- "$(command -v -- "$BASE")")" && pwd -P )

export LD_LIBRARY_PATH=/net/mmx/mnt/app/root/lib-target:/net/mmx/eso/lib:/net/mmx/mnt/app/usr/lib:/net/mmx/mnt/app/armle/lib:/net/mmx/mnt/app/armle/lib/dll:/net/mmx/mnt/app/armle/usr/lib

mount -uw /mnt/app

JAR_TARGET=/mnt/app/eso/hmi/lsd/jars/
mkdir -p ${JAR_TARGET}

cp -v ${SCRIPTDIR}/VCAndroidAuto.jar ${JAR_TARGET}

LSD=/mnt/app/eso/hmi/lsd/lsd.sh
BU=/mnt/app/eso/hmi/lsd/lsd.sh.bu

if [ -e $BU ]; then
mv $BU $LSD
fi

if ! grep -q VCAndroidAuto.jar ${LSD}; then
if [ ! -e $BU ]; then
  echo "Backup lsd.sh"
  cp -v $LSD $BU
fi
echo "Patching lsd.sh to run VCAndroidAuto.jar"
sed -ir 's,^$J9,BOOTCLASSPATH="$BOOTCLASSPATH -Xbootclasspath/p:$BASE_DIR/lsd/jars/VCAndroidAuto.jar"\n$J9,g' $LSD
fi

mount -ur /mnt/app
echo "Done, please restart headunit"
