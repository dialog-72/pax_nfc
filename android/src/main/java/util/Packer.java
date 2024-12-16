/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology (Shenzhen) Co., Ltd. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology (Shenzhen) Co., Ltd. and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2017-2023 PAX Computer Technology (Shenzhen) Co., Ltd. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date                         Author                        Action
 * 2017/12/20                   PAX                     Create/Add/Modify/Delete
 * ===========================================================================================
 */

package util;

import android.content.Context;

public class Packer implements IPacker {
    private static Packer instance;
    private Context context;

    private Packer() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public synchronized static Packer getInstance() {
        if (instance == null) {
            instance = new Packer();
        }

        return instance;
    }

    @Override
    public IApdu getApdu() {
        PackerApdu apdu = PackerApdu.getInstance();
        apdu.setContext(context);
        return apdu;
    }

}
