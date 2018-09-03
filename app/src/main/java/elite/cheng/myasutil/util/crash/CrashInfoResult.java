package elite.cheng.myasutil.util.crash;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;


public class CrashInfoResult {

    private boolean result;
    private List<CrashItem> dataList;

    public CrashInfoResult() {
        this.dataList = new LinkedList<>();
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<CrashItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<CrashItem> dataList) {
        this.dataList = dataList;
    }

    /**
     * @return
     */
    public String toJson() {
        return new Gson().toJson(this);
    }


    public static class CrashItem {

        private String androidId;
        private String stackInfo;
        private String time;
        private long timestamp;
        private String versionCode;
        private String versionName;
        private DeviceInfo deviceInfo;

        public String getAndroidId() {
            return androidId;
        }

        public void setAndroidId(String androidId) {
            this.androidId = androidId;
        }

        public String getStackInfo() {
            return stackInfo;
        }

        public void setStackInfo(String stackInfo) {
            this.stackInfo = stackInfo;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public DeviceInfo getDeviceInfo() {
            return deviceInfo;
        }

        public void setDeviceInfo(DeviceInfo deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

        public String toJson() {
            return new Gson().toJson(this);
        }
    }

    public static class DeviceInfo {
        private String brand;
        private String display;
        private String model;
        private String versionRelease;
        private int SDKInt;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getVersionRelease() {
            return versionRelease;
        }

        public void setVersionRelease(String versionRelease) {
            this.versionRelease = versionRelease;
        }

        public int getSDKInt() {
            return SDKInt;
        }

        public void setSDKInt(int SDKInt) {
            this.SDKInt = SDKInt;
        }
    }
}
