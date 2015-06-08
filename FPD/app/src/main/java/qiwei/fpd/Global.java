package qiwei.fpd;

import android.app.Application;

/**
 * Created by Qiwei on 6/8/15.
 */

public class Global extends Application {
    private String mastername;
    private String tmpString;
    private String[] tmpStringArray;
    private Boolean tmpFlag;

    public String getMastername() {
        return mastername;
    }

    public void setMastername(String mastername) {
        this.mastername = mastername;
    }

    public String getTmpString() {
        return tmpString;
    }

    public void setTmpString(String tmpString) {
        this.tmpString = tmpString;
    }

    public String[] getTmpStringArray() {
        return tmpStringArray;
    }

    public void setTmpStringArray(String[] tmpStringArray) {
        this.tmpStringArray = tmpStringArray;
    }

    public Boolean getTmpFlag() {
        return tmpFlag;
    }

    public void setTmpFlag(Boolean tmpFlag) {
        this.tmpFlag = tmpFlag;
    }
}


