package com.wafej.btcontroller;

import java.util.UUID;

/**
 * Created by wafej on 2018/1/23.
 */

public class BTUtils {

    public static final UUID PRIVATE_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB"/*"0000110a-0000-1000-8000-00805f9b34fb"*/);//00001101-0000-1000-8000-00805F9B34FBUUID.fromString("0f3561b9-bda5-4672-84ff-ab1f98e349b6");

    public static final int MESSAGE_CONNECT_SUCCESS = 0x00000001;

    public static final int MESSAGE_CONNECT_ERROR = 0x00000002;
}
