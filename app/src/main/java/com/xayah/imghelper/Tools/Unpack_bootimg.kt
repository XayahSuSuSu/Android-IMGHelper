package com.xayah.imghelper.Tools

import android.util.Log
import com.topjohnwu.superuser.Shell

class Unpack_bootimg() {
    /*
    usage: unpack_bootimg [-h] --boot_img BOOT_IMG [--out OUT]

    Unpacks boot.img/recovery.img, extracts the kernel,ramdisk, second bootloader,
    recovery dtbo and dtb

    optional arguments:
    -h, --help           show this help message and exit
    --boot_img BOOT_IMG  path to boot image
    --out OUT            path to out binaries
     */
    companion object {
        fun unpackIMG(BOOT_IMG: String, OUT: String) {
            val unpackIMG = Shell.su("unpack_bootimg --boot_img $BOOT_IMG --out $OUT").exec().out
            Log.d("unpackIMG", unpackIMG.toString())
        }

        fun unpackRamdisk(ramdiskPath: String) {
            Shell.su("mv ${ramdiskPath}/ramdisk ${ramdiskPath}/ramdisk.cpio.gz").exec()
            Shell.su("mkdir ${ramdiskPath}/ramdisk").exec()
            Shell.su("mv ${ramdiskPath}/ramdisk.cpio.gz ${ramdiskPath}/ramdisk/ramdisk.cpio.gz")
                .exec()
            Shell.su("gunzip ${ramdiskPath}/ramdisk/ramdisk.cpio.gz").exec()
            Shell.su("cd ${ramdiskPath}/ramdisk").exec()
            Shell.su("cpio -idmv < ramdisk.cpio").exec()
            Shell.su("rm ramdisk.cpio").exec()
        }

    }

}