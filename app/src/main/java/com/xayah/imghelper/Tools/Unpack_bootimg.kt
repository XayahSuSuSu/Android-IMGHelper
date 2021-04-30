package com.xayah.imghelper.Tools

import com.xayah.imghelper.Utils.CommandUtil

class Unpack_bootimg(var toolPath: String) {
    /*
    usage: unpack_bootimg [-h] --boot_img BOOT_IMG [--out OUT]

    Unpacks boot.img/recovery.img, extracts the kernel,ramdisk, second bootloader,
    recovery dtbo and dtb

    optional arguments:
    -h, --help           show this help message and exit
    --boot_img BOOT_IMG  path to boot image
    --out OUT            path to out binaries
     */
    fun unpack(BOOT_IMG: String, OUT: String) {
        CommandUtil.executeCommand(
            "./unpack_bootimg --boot_img $BOOT_IMG --out $OUT",
            toolPath,
            true,
            true
        )// unpack boot.img
    }
}